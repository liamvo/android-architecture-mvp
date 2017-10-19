package com.liveteamvn.archmvp.base.fragment;


import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.liveteamvn.archmvp.base.fragment.IFragment;

import java.util.ArrayList;

/**
 * Created by Liam Vo on 3/3/17.
 */

public interface IFragmentState {

    void setStacksRootFragment(ArrayList<IFragment> fragments);

    void changeRootFragment(IFragment fragments, int stackId);

    boolean isRootFragment();

    void pushFragment(IFragment fragment);

    void pushFragment(IFragment fragment, boolean ignoreDuplicate);

    void pushFragmentKeepOld(IFragment fragment);

    void pushFragmentKeepOld(IFragment fragment, boolean ignoreDuplicate);

    void popFragment(int numberPop);

    void showStack(int stackId);

    void refreshStack(int stackId);

    void replaceFragment(IFragment fragment);

    void clearStack(int stackId);

    void clearAllStacks();

    Fragment getFragByTag(String tag);

    FragmentManager getFragmentManager();

    FragmentTransaction beginTrans();

    void setCustomAnimation(@AnimatorRes @AnimRes int inAnim, @AnimatorRes @AnimRes int outAnim);

    void enableAnim(boolean enable);

    void resetAnimation();
}
