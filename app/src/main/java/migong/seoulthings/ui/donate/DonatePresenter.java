package migong.seoulthings.ui.donate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.collect.Lists;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import migong.seoulthings.R;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.data.Donation;
import migong.seoulthings.ui.Presenter;

import org.apache.commons.lang3.StringUtils;

public class DonatePresenter implements Presenter {

    private static final String TAG = DonatePresenter.class.getSimpleName();

    private boolean mShowGoogleMap;
    @Nullable
    private String mDonationId;
    private LatLng mLastLatLng;
    private String mLastThoroughfare;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    @Nullable
    private DocumentReference mReference;
    private FirebaseStorage mStorage;
    @NonNull
    private final CompositeDisposable mCompositeDisposable;
    @NonNull
    private final DonateView mView;
    @NonNull
    private final Executor executor = Executors.newSingleThreadExecutor();

    private Donation donation;

    public DonatePresenter(@NonNull DonateView view, @Nullable String donationId) {
        mView = view;
        mDonationId = donationId;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();

        if (StringUtils.isNotEmpty(mDonationId)) {
            mReference = mFirestore.collection("donations").document(mDonationId);
            mReference.get()
                    .addOnFailureListener(error -> Log.e(TAG, "Failed to get snapshot.", error))
                    .addOnSuccessListener(snapshot -> {
                        final Donation donation = snapshot.toObject(Donation.class);
                        if (donation == null) {
                            Log.e(TAG, "onResume: donation is NULL.");
                            return;
                        }
                        Log.d(TAG, "onResume: donation is " + donation);

                        mView.showCompleteButton();
                        mView.setDonationTitle(donation.getTitle());
                        mView.setDonationContents(donation.getContents());
                        if (donation.getImageUrls() != null) {
                            mView.setDonationImages(Lists.transform(donation.getImageUrls(), Uri::parse));
                            //여기에 썸네일이미지uri값을 같이 넘겨줘야한다!!!!!
                            //여기야!!친구야!!!다른곳을 보지말고 여길봐야해!!!
                        }
                        onGoogleMapClicked(new LatLng(donation.getLatitude(), donation.getLongitude()));
                    });
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
    }

    public void onSubmitButtonClicked() {
        if (mUser == null) {
            return;
        }
        if (mLastLatLng == null) {
            mView.showSnackBar(R.string.msg_invalid_location);
            return;
        }
        if (StringUtils.isEmpty(mLastThoroughfare)) {
            mView.showSnackBar(R.string.msg_invalid_thoroughfare);
            return;
        }
        if (StringUtils.isEmpty(mView.getDonationTitle())) {
            mView.showSnackBar(R.string.msg_invalid_title);
            return;
        }
        if (StringUtils.isEmpty(mView.getDonationContents())) {
            mView.showSnackBar(R.string.msg_invalid_contents);
            return;
        }
        if (mView.getDonationImages().size() == 0) {
            mView.showSnackBar(R.string.msg_invalid_images);
            return;
        }
        mView.startSubmit();

        if (StringUtils.isEmpty(mDonationId)) {
            mCompositeDisposable.add(
                    createDonation()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    mView::finish,
                                    error -> {
                                        Log.e(TAG, "Failed to create donation.", error);

                                        mView.finishSubmit();
                                        mView.showSnackBar(R.string.msg_upload_donation_fail);
                                    }
                            )
            );
        } else {
            mCompositeDisposable.add(
                    updateDonation()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    mView::finish,
                                    error -> {
                                        Log.e(TAG, "Failed to update donation.", error);

                                        mView.finishSubmit();
                                        mView.showSnackBar(R.string.msg_upload_donation_fail);
                                    }
                            )
            );
        }
    }

    public void onCompleteButtonClicked() {
        if (mReference != null) {
            mReference.update("complete", true)
                    .addOnFailureListener(error -> {
                        Log.e(TAG, "Failed to complete donation.", error);

                        mView.finishSubmit();
                        mView.showSnackBar(R.string.msg_complete_donation_fail);
                    })
                    .addOnSuccessListener(v -> mView.finish());
        }
    }

    public void onAddPhotoButtonClicked() {
        mView.startPickPhotoIntent();
    }

    public void onRequestPickPhotoSuccess(Intent data) {
        Uri photoUri = data.getData();
        if (photoUri == null) {
            Log.e(TAG, "onRequestPickPhotoSuccess: photoUri is NULL.");
            return;
        }

        mView.addImage(photoUri);
    }

    public void onAddressButtonClicked() {
        if (mShowGoogleMap) {
            mView.hideGoogleMap();
            mShowGoogleMap = false;
        } else {
            mView.showGoogleMap();
            mShowGoogleMap = true;
        }
    }

    public void onGoogleMapClicked(@NonNull LatLng latLng) {
        mView.startAddressLoading();

        mLastLatLng = latLng;
        mView.setMarkerOnGoogleMap(latLng);

        try {
            final Geocoder geocoder = new Geocoder(mView.getContext());
            final List<Address> addresses = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                mLastThoroughfare = address.getThoroughfare();
            } else {
                mLastThoroughfare = StringUtils.EMPTY;
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed get from location " + latLng, e);
        } finally {
            mView.changeAddressButtonText(mLastThoroughfare);
            mView.finishAddressLoading();
        }
    }

    private Completable createDonation() {
        return Completable.create(emitter -> {
            donation = new Donation(mUser.getUid(), mView.getDonationTitle(),
                    mView.getDonationContents(), mLastThoroughfare, mLastLatLng);
            mFirestore.collection("donations")
                    .add(donation)
                    .continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            if (task.getException() == null) {
                                throw new Exception("Failed to add a donation to Firestore.");
                            } else {
                                throw task.getException();
                            }
                        }

                        final DocumentReference reference = task.getResult();
                        final String id = reference.getId();
                        return Tasks.whenAll(uploadThumbnail(reference, id), uploadImages(reference, id));
                    })
                    .addOnSuccessListener(v -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    private Completable updateDonation() {
        return Completable.create(emitter -> {
            if (StringUtils.isEmpty(mDonationId) || mReference == null) {
                emitter.onComplete();
                return;
            }

            mReference
                    .update(
                            "title", mView.getDonationTitle(),
                            "contents", mView.getDonationContents(),
                            "dong", mLastThoroughfare,
                            "thumbnailUrl", null,
                            "imageUrls", null,
                            "latitude", mLastLatLng.latitude,
                            "longitude", mLastLatLng.longitude,
                            "updatedAt", Timestamp.now()
                    )
                    .continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            if (task.getException() == null) {
                                throw new Exception("Failed to add a donation to Firestore.");
                            } else {
                                throw task.getException();
                            }
                        }

                        return Tasks.whenAll(
                                uploadThumbnail(mReference, mDonationId),
                                uploadImages(mReference, mDonationId)
                        );
                    })
                    .addOnSuccessListener(v -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    private Task<Void> uploadThumbnail(DocumentReference reference, String id) throws IOException {
        final StorageReference storageRef = mStorage.getReference();
        final StorageReference donationsRef = storageRef.child("donations");
        final StorageReference donationRef = donationsRef.child(id);
        final StorageReference thumbnailRef = donationRef.child("thumbnail");

        final Uri thumbnail = mView.getDonationImages().get(0);
        return Tasks.call(executor, transformThumbnailImage(thumbnail))
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() == null) {
                            throw new Exception("Failed to make thumbnail.");
                        } else {
                            throw task.getException();
                        }
                    }

                    return thumbnailRef.putBytes(task.getResult());
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() == null) {
                            throw new Exception("Failed to upload thumbnail to Firebase Storage.");
                        } else {
                            throw task.getException();
                        }
                    }

                    return thumbnailRef.getDownloadUrl();
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() == null) {
                            throw new Exception("Failed to upload get downloadurl from Firebase Storage.");
                        } else {
                            throw task.getException();
                        }
                    }

                    return reference.update("thumbnailUrl", task.getResult().toString());
                });
    }

    private Task<Void> uploadImages(DocumentReference reference, String id) {
        final List<Task<Uri>> tasks = new ArrayList<>();
        final List<Uri> images = mView.getDonationImages();

        for (int i = 0; i < images.size(); i++) {
            tasks.add(uploadImage(id, i, images.get(i)));
        }

        return Tasks.whenAllSuccess(tasks)
                .continueWithTask(task -> {
                    final List<String> urls = new ArrayList<>();

                    for (Object uri : task.getResult()) {
                        urls.add(uri.toString());
                    }

                    return reference.update("imageUrls", urls);
                });
    }

    private Task<Uri> uploadImage(String id, int index, Uri imageUri) {
        final StorageReference storageRef = mStorage.getReference();
        final StorageReference donationsRef = storageRef.child("donations");
        final StorageReference donationRef = donationsRef.child(id);
        final StorageReference imageRef = donationRef.child(String.valueOf(index));
        return Tasks.call(executor, transformImage(imageUri))
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() == null) {
                            throw new Exception("Failed to transformImage image.");
                        } else {
                            throw task.getException();
                        }
                    }

                    return imageRef.putBytes(task.getResult());
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() == null) {
                            throw new Exception("Failed to upload image to Firebase Storage.");
                        } else {
                            throw task.getException();
                        }
                    }

                    return imageRef.getDownloadUrl();
                });
    }

    private Callable<byte[]> transformThumbnailImage(Uri uri) {
        return () -> {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                final Bitmap bitmap = Picasso.get()
                        .load(uri)
                        .resize(SeoulThingsConstants.THUMBNAIL_SIZE, SeoulThingsConstants.THUMBNAIL_SIZE)
                        .get();
                bitmap.compress(CompressFormat.JPEG, 100, outputStream);
                return outputStream.toByteArray();
            }
        };
    }

    private Callable<byte[]> transformImage(Uri uri) {
        return () -> {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                final Bitmap bitmap = Picasso.get().load(uri).get();
                bitmap.compress(CompressFormat.JPEG, 100, outputStream);
                return outputStream.toByteArray();
            }
        };
    }

}
