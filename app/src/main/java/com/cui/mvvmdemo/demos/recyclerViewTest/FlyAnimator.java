package com.cui.mvvmdemo.demos.recyclerViewTest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.List;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

/**
 * recyclerView item 飞入飞出动画
 */
public class FlyAnimator extends SimpleItemAnimator {
    List<RecyclerView.ViewHolder> removeHolders = new ArrayList<>();
    List<RecyclerView.ViewHolder> removeAnimators = new ArrayList<>();

    List<RecyclerView.ViewHolder> addHolders = new ArrayList<>();
    List<RecyclerView.ViewHolder> addAnimators = new ArrayList<>();

    List<RecyclerView.ViewHolder> moveHolders = new ArrayList<>();
    List<RecyclerView.ViewHolder> moveAnimators = new ArrayList<>();


    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        removeHolders.add(holder);
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        addHolders.add(holder);
        return true;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            holder.itemView.setTranslationY(fromY - toY);
        }
        moveHolders.add(holder);
        return true;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }

    @Override
    public void runPendingAnimations() {
        if(!removeHolders.isEmpty()) {
            for(RecyclerView.ViewHolder holder : removeHolders) {
                remove(holder);
            }
            removeHolders.clear();
        }

        if(!addHolders.isEmpty()) {
            for(RecyclerView.ViewHolder holder : addHolders) {
                add(holder);
            }
            addHolders.clear();
        }

        if(!moveHolders.isEmpty()){
            for(RecyclerView.ViewHolder holder : moveHolders) {
                move(holder);
            }
            moveHolders.clear();
        }
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
    }

    @Override
    public void endAnimations() {
    }

    @Override
    public boolean isRunning() {

        return !(removeHolders.isEmpty() && removeAnimators.isEmpty() && addHolders.isEmpty() && addAnimators.isEmpty() && moveHolders.isEmpty() && moveAnimators.isEmpty());
    }

    private void remove(final RecyclerView.ViewHolder holder){
        final View view = holder.itemView;
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);

        removeAnimators.add(holder);

        ViewCompat.setAlpha(view, 1);
        ViewCompat.setTranslationX(view, 0);
        animation.alpha(0).setDuration(500).translationX(1000).setDuration(500).setListener(new ViewPropertyAnimatorListenerAdapter(){

            @Override
            public void onAnimationStart(View view) {
                dispatchRemoveStarting(holder);
            }

            @Override
            public void onAnimationEnd(View view) {
                removeAnimators.remove(holder);
                dispatchRemoveFinished(holder);
                if(!isRunning()){
                    dispatchAnimationsFinished();
                }
            }

            @Override
            public void onAnimationCancel(View view) {
                ViewCompat.setAlpha(view, 0);
            }
        }).start();
//        removeAnimators.add(holder);
//        TranslateAnimation animation = new TranslateAnimation(0, 1000, 0, 0);
//        animation.setDuration(500);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                dispatchRemoveStarting(holder);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                removeAnimators.remove(holder);
//                dispatchRemoveFinished(holder);
//                if(!isRunning()){
//                    dispatchAnimationsFinished();
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        holder.itemView.startAnimation(animation);
    }

    private void add(final RecyclerView.ViewHolder holder){
        final View view = holder.itemView;
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);

        addAnimators.add(holder);

        ViewCompat.setAlpha(view, 0);
        ViewCompat.setTranslationX(view, 1000);
        animation.alpha(1).setDuration(500).translationX(0).setDuration(500).setListener(new ViewPropertyAnimatorListenerAdapter(){

            @Override
            public void onAnimationStart(View view) {
                dispatchAddStarting(holder);
            }

            @Override
            public void onAnimationEnd(View view) {
                addAnimators.remove(holder);
                dispatchAddFinished(holder);
                if(!isRunning()){
                    dispatchAnimationsFinished();
                }
            }

            @Override
            public void onAnimationCancel(View view) {
                ViewCompat.setAlpha(view, 1);
            }
        }).start();
//        TranslateAnimation animation = new TranslateAnimation(1000, 0, 0, 0);
//        animation.setDuration(500);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                dispatchAddStarting(holder);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                addAnimators.remove(holder);
//                dispatchAddFinished(holder);
//                if(!isRunning()){
//                    dispatchAnimationsFinished();
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        holder.itemView.startAnimation(animation);
    }


    private void move(final RecyclerView.ViewHolder holder){
        moveAnimators.add(holder);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(holder.itemView,
                    "translationY", holder.itemView.getTranslationY(), 0);
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    dispatchMoveStarting(holder);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    dispatchMoveFinished(holder);
                    moveAnimators.remove(holder);
                    if(!isRunning()) dispatchAnimationsFinished();
                }
            });
            animator.start();
        }

    }

}
