package migong.seoulthings.ui.main.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import migong.seoulthings.R;
import migong.seoulthings.data.Category;
import migong.seoulthings.ui.main.category.things.ThingsActivity;
import migong.seoulthings.ui.main.category.things.ThingsView;
import migong.seoulthings.ui.search.SearchActivity;
import migong.seoulthings.ui.search.SearchView;

public class CategoryFragment extends Fragment implements CategoryView {

    private ImageButton mBicycleCategoryButton;
    private ImageButton mToyCategoryButton;
    private ImageButton mToolCategoryButton;
    private ImageButton mMedicalDeviceCategoryButton;
    private ImageButton mPowerBankCategoryButton;
    private ImageButton mSuitCategoryButton;

    private CategoryPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBicycleCategoryButton = view.findViewById(R.id.category_button_bicycle);
        mBicycleCategoryButton.setOnClickListener(v -> {
            mPresenter.onCategoryButtonClicked(Category.BICYCLE);
        });

        mToyCategoryButton = view.findViewById(R.id.category_button_toy);
        mToyCategoryButton.setOnClickListener(v -> {
            mPresenter.onCategoryButtonClicked(Category.TOY);
        });

        mToolCategoryButton = view.findViewById(R.id.category_button_tool);
        mToolCategoryButton.setOnClickListener(v -> {
            mPresenter.onCategoryButtonClicked(Category.TOOL);
        });

        mMedicalDeviceCategoryButton = view.findViewById(R.id.category_button_medical_device);
        mMedicalDeviceCategoryButton.setOnClickListener(v -> {
            mPresenter.onCategoryButtonClicked(Category.MEDICAL_DEVICE);
        });

        mPowerBankCategoryButton = view.findViewById(R.id.category_button_power_bank);
        mPowerBankCategoryButton.setOnClickListener(v -> {
            mPresenter.onCategoryButtonClicked(Category.POWER_BANK);
        });

        mSuitCategoryButton = view.findViewById(R.id.category_button_suit);
        mSuitCategoryButton.setOnClickListener(v -> {
            mPresenter.onCategoryButtonClicked(Category.SUIT);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new CategoryPresenter(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void startSearchActivity() {
        Intent intent = new Intent(getContext(), SearchActivity.class);

        Bundle args = new Bundle();
        args.putString(SearchView.KEY_SCOPE, SearchView.SCOPE_THINGS);
        args.putString(SearchView.KEY_CATEGORY, Category.ALL);
        intent.putExtras(args);

        startActivity(intent);
    }

    @Override
    public void startThingsActivity(@NonNull String category) {
        Intent intent = new Intent(getContext(), ThingsActivity.class);

        Bundle args = new Bundle();
        args.putString(ThingsView.KEY_CATEGORY, category);
        intent.putExtras(args);

        startActivity(intent);
    }
}
