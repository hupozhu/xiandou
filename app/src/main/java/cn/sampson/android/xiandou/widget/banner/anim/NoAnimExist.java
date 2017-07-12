package cn.sampson.android.xiandou.widget.banner.anim;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class NoAnimExist extends BaseAnimator {
    public NoAnimExist() {
        this.mDuration = 200;
    }

    public void setAnimation(View view) {
        this.mAnimatorSet.playTogether(new Animator[]{
                ObjectAnimator.ofFloat(view, "alpha", 1, 1)});
    }
}
