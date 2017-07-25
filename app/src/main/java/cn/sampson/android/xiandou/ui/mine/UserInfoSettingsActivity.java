package cn.sampson.android.xiandou.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.Constants;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.TextInputLayoutFix;

/**
 * Created by chengyang on 2017/7/11.
 */

public class UserInfoSettingsActivity extends BaseActivity {

    public static final String TYPE = "type";
    public static final String CONTENT = "content";

    public static final int INPUT_NICKNAME = 1;
    public static final int INPUT_ADDRESS = 2;

    @Bind(R.id.et_info)
    TextInputEditText etInfo;
    @Bind(R.id.input_layout_info)
    TextInputLayoutFix inputLayoutInfo;
    @Bind(R.id.rl_clear)
    FrameLayout rlClear;

    int type;
    String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_user_info_settings);
        ButterKnife.bind(this);
        setActionBarBack();

        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra(TYPE, 0);
        switch (type) {
            case INPUT_NICKNAME:
                inputLayoutInfo.setHint(getString(R.string.input_nickname));
                break;
            case INPUT_ADDRESS:
                inputLayoutInfo.setHint(getString(R.string.input_address));
                break;
        }
        content = getIntent().getStringExtra(CONTENT);
        if (!TextUtils.isEmpty(content)) {
            etInfo.setText(content);
            etInfo.setSelection(content.length());
            rlClear.setVisibility(View.VISIBLE);
        }

        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputLayoutInfo.setError(null);
                inputLayoutInfo.setErrorEnabled(false);

                if (editable.length() > 0) {
                    rlClear.setVisibility(View.VISIBLE);
                } else {
                    rlClear.setVisibility(View.GONE);
                }
            }
        });

        rlClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etInfo.setText("");
                rlClear.setVisibility(View.GONE);
            }
        });

    }

    public String getTextInput() {
        String input = etInfo.getText().toString();

        switch (type) {
            case INPUT_NICKNAME:
                if (TextUtils.isEmpty(input)) {
                    inputLayoutInfo.setErrorEnabled(true);
                    inputLayoutInfo.setError(getString(R.string.input_nickname_not_null));
                    requestFocus(etInfo);
                    return null;
                }
                break;
            case INPUT_ADDRESS:
                if (TextUtils.isEmpty(input)) {
                    inputLayoutInfo.setErrorEnabled(true);
                    inputLayoutInfo.setError(getString(R.string.input_nickname_not_null));
                    requestFocus(etInfo);
                    return null;
                }
                break;
        }
        return input;
    }

    @Override
    protected void onToolbarSave() {
        String inputString = getTextInput();
        if (TextUtils.isEmpty(inputString)) {
            return;
        }

        if (!TextUtils.equals(inputString, content)) {
            Intent requestIntent = new Intent();
            requestIntent.putExtra("data", inputString);
            setResult(Constants.ACTIVITY_RESULT_SUCCESS, requestIntent);
            goBack();
        } else {
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

}
