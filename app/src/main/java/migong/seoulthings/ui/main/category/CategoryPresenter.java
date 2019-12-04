package migong.seoulthings.ui.main.category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import migong.seoulthings.data.Category;
import migong.seoulthings.ui.Presenter;

public class CategoryPresenter implements Presenter {

    private static final String TAG = CategoryPresenter.class.getSimpleName();

    @NonNull
    private final CategoryView mView;

    public CategoryPresenter(@NonNull CategoryView view) {
        this.mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
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

    public void onCategoryButtonClicked(@NonNull String category) {
        switch (category) {
            case Category.BICYCLE:
            case Category.MEDICAL_DEVICE:
            case Category.POWER_BANK:
            case Category.SUIT:
            case Category.TOOL:
            case Category.TOY:
                mView.startThingsActivity(category);
                break;
            default:
                // TODO(@gihwan): 올바르지 않은 카테고리
                break;
        }
    }
}
