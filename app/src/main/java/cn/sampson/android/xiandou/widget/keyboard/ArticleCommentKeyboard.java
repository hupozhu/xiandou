package cn.sampson.android.xiandou.widget.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.utils.UiUtils;

/**
 * Created by chengyang on 2017/7/18.
 */

public class ArticleCommentKeyboard extends CommonKeyboard {

    protected EditText etInput;
    protected TextView tvComment;

    public ArticleCommentKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getKeyboardLayoutId() {
        return R.layout.keyboard_article_comment;
    }

    @Override
    public void initView() {
        etInput = UiUtils.find(this, R.id.et_input);
        tvComment = UiUtils.find(this, R.id.tv_comment);

        //为输入框添加触摸响应事件
        etInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (!etInput.isFocused()) {
                    etInput.setFocusable(true);
                    etInput.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

    }

    public EditText getInput() {
        return etInput;
    }

    public TextView getComment() {
        return tvComment;
    }
}
