package com.xilinzhang.ocr.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;

import com.xilinzhang.ocr.R;

public class LevelUtils {
    public static int level;
    public static int exp;

    public static int getNewLevelExp() {
        return (level + 1) * (level + 1);
    }

    public static void addExpWhenSearch(Context context, FrameLayout view) {
        exp += 2;
        ifUpgrade(context, view);
        updateExp();
    }

    public static void addExpWhenPutUpQuestion(Context context, FrameLayout view) {
        exp += 4;
        ifUpgrade(context, view);
        updateExp();
    }

    public static void addExpWhenDoQuestion(Context context, FrameLayout view) {
        exp += 8;
        ifUpgrade(context, view);
        updateExp();
    }

    public static void addExpDaily(Context context, FrameLayout view) {
        exp += 2;
        ifUpgrade(context, view);
        updateExp();
    }

    private static void ifUpgrade(final Context context , final FrameLayout view) {
        if (exp >= getNewLevelExp()) {
            exp -= getNewLevelExp();
            level += 1;
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getLevelUpAnimator(context, view).start();
                }
            });
            //TODO some animation
            ifUpgrade(context, view);
        }
    }

    private static void updateExp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkUtils.setExp();
            }
        }).start();
    }

    public static AnimatorSet getLevelUpAnimator(Context context, final FrameLayout view) {
        final AppCompatImageView imageView = new AppCompatImageView(context);
        imageView.setImageResource(R.drawable.level_up);

        ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(
                imageView,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f)
        );
        scaleAnimator.setDuration(1000);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(
                imageView,
                View.ALPHA,
                1f,
                0f
        );
        alphaAnimator.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(scaleAnimator, alphaAnimator);

        view.addView(imageView);
        FrameLayout.LayoutParams fllp = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        fllp.gravity = Gravity.CENTER;

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.removeView(imageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return animatorSet;
    }
}
