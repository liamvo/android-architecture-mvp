package com.liveteamvn.archmvp.base.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.liveteamvn.archmvp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Liam Vo on 3/3/17.
 */

public class FragmentState implements IFragmentState {
    private FragmentManager fragmentManager;
    private int idContent = R.id.fragContainer;
    private int stackSelected = -1;
    private int tagCount;
    private List<Stack<IFragment>> stacksFragment;
    private ArrayList<IFragment> rootFragments;
    private List<String> fragmentsKeepAlive;
    private String crrView = "";
    private boolean enableAnim = true;
    private int inAnim = R.anim.fade_in;
    private int outAnim = R.anim.fade_out;

    public FragmentState(FragmentManager fragmentManager, int idContent) {
        this.fragmentManager = fragmentManager;
        this.idContent = idContent;
        stacksFragment = new ArrayList<>();
        fragmentsKeepAlive = new ArrayList<>();
    }

    @Override
    public void setStacksRootFragment(ArrayList<IFragment> fragments) {
        for (IFragment ignored : fragments) {
            stacksFragment.add(new Stack<>());
        }
        rootFragments = fragments;
    }

    @Override
    public void changeRootFragment(IFragment fragments, int stackId) {
        executeFragTransaction(null, fragmentTransaction -> {
            clearStack(stackId, fragmentTransaction);
            if (stacksFragment.get(stackId).size() > 0) {
                fragmentTransaction.remove(stacksFragment.get(stackId).pop());
            }
            rootFragments.set(stackId, fragments);
            refreshStack(stackId, fragmentTransaction);
        });
    }

    @Override
    public boolean isRootFragment() {
        return stacksFragment.get(stackSelected).size() <= 1;
    }

    @Override
    public void pushFragment(IFragment fragment) {
        if (!checkFragDuplicate(fragment)) {
            executeFragTransaction(null, fragmentTransaction -> pushFrag(fragment, fragmentTransaction));
        }
    }

    @Override
    public void pushFragment(IFragment fragment, boolean ignoreDuplicate) {
        if (ignoreDuplicate) {
            executeFragTransaction(null, fragmentTransaction -> pushFrag(fragment, fragmentTransaction));
        } else {
            pushFragment(fragment);
        }
    }

    @Override
    public void pushFragmentKeepOld(IFragment fragment) {
        if (!checkFragDuplicate(fragment)) {
            executeFragTransaction(null, fragmentTransaction -> pushFragKeepOld(fragment, fragmentTransaction));
        }
    }

    @Override
    public void pushFragmentKeepOld(IFragment fragment, boolean ignoreDuplicate) {
        if (ignoreDuplicate) {
            executeFragTransaction(null, fragmentTransaction -> pushFragKeepOld(fragment, fragmentTransaction));
        } else {
            pushFragmentKeepOld(fragment);
        }
    }

    @Override
    public void popFragment(int numberPop) {
        crrView = "";
        if (numberPop >= stacksFragment.get(stackSelected).size()) {
            throw new StringIndexOutOfBoundsException("Number pop out of stack size");
        }
        executeFragTransaction(null, fragmentTransaction -> {
            for (int index = 0; index < numberPop; index++) {
                fragmentsKeepAlive.remove(stacksFragment.get(stackSelected).peek().getTag());
                fragmentTransaction.remove(stacksFragment.get(stackSelected).pop());
            }
            if (!fragmentsKeepAlive.contains(stacksFragment.get(stackSelected).peek().getTag())) {
                fragmentTransaction.attach(stacksFragment.get(stackSelected).peek());
            } else {
                fragmentTransaction.show(stacksFragment.get(stackSelected).peek());
                fragmentsKeepAlive.remove(stacksFragment.get(stackSelected).peek().getTag());
            }
        });
    }

    @Override
    public void showStack(int stackId) {
        if (stackId != stackSelected) {
            executeFragTransaction(null, fragmentTransaction -> {
                attachStack(stackId, fragmentTransaction);
                detachPrevStack(fragmentTransaction);
                stackSelected = stackId;
            });
        }
    }

    @Override
    public void refreshStack(int stackId) {
        refreshStack(stackId, null);
    }

    @Override
    public void replaceFragment(IFragment fragment) {
        executeFragTransaction(null, fragmentTransaction -> fragmentTransaction.replace(idContent, fragment, generateTag(fragment)));
        stacksFragment.get(stackSelected).pop();
        stacksFragment.get(stackSelected).push(fragment);
    }

    @Override
    public void clearStack(int stackId) {
        executeFragTransaction(null, fragmentTransaction -> clearStack(stackId, fragmentTransaction));
    }

    @Override
    public void clearAllStacks() {
        executeFragTransaction(null, fragmentTransaction -> {
            for (Stack<IFragment> stack : stacksFragment) {
                clearStack(stacksFragment.indexOf(stack), fragmentTransaction);
            }
        });
    }

    @Override
    public Fragment getFragByTag(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    @Override
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    @Override
    public void setCustomAnimation(int inAnim, int outAnim) {
        this.inAnim = inAnim;
        this.outAnim = outAnim;
    }

    @Override
    public void enableAnim(boolean enable) {
        enableAnim = enable;
    }

    @Override
    public void resetAnimation() {
        inAnim = R.anim.fade_in;
        outAnim = R.anim.fade_out;
    }

    @SuppressLint("CommitTransaction")
    public FragmentTransaction beginTrans() {
        if (enableAnim) {
            return fragmentManager.beginTransaction().setCustomAnimations(inAnim, outAnim);
        } else {
            return fragmentManager.beginTransaction();
        }
    }

    private void executeFragTransaction(FragmentTransaction fragmentTransactionPending, IExecuteAction executeAction) {
        FragmentTransaction fragmentTransaction = fragmentTransactionPending == null ? beginTrans() : fragmentTransactionPending;
        executeAction.execute(fragmentTransaction);
        if (fragmentTransactionPending == null) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private boolean checkFragDuplicate(IFragment fragment) {
        return crrView.equals(fragment.getClass().getSimpleName());
    }

    private void pushFrag(IFragment fragment, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.add(idContent, fragment, generateTag(fragment));
        detachCurrentFrag(fragmentTransaction);
        stacksFragment.get(stackSelected).push(fragment);
        crrView = fragment.getClass().getSimpleName();
    }

    private void pushFragKeepOld(IFragment fragment, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.hide(stacksFragment.get(stackSelected).peek()).add(idContent, fragment, generateTag(fragment));
        fragmentsKeepAlive.add(stacksFragment.get(stackSelected).peek().getTag());
        stacksFragment.get(stackSelected).push(fragment);
        crrView = fragment.getClass().getSimpleName();
    }

    private String generateTag(IFragment fragment) {
        return fragment.getClass().getName() + '_' + ++tagCount;
    }

    private void attachStack(int stackId, FragmentTransaction fragmentTransaction) {
        if (stacksFragment.get(stackId).size() == 0) {
            initStack(stackId, fragmentTransaction);
        } else {
            fragmentTransaction.attach(stacksFragment.get(stackId).peek());
        }
    }

    private void detachPrevStack(FragmentTransaction fragmentTransaction) {
        if (stackSelected != -1) {
            detachCurrentFrag(fragmentTransaction);
        }
    }

    private void detachCurrentFrag(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.detach(stacksFragment.get(stackSelected).peek());
    }

    private void initStack(int stackId, FragmentTransaction fragmentTransaction) {
        stacksFragment.get(stackId).push(rootFragments.get(stackId));
        fragmentTransaction.add(
                idContent,
                stacksFragment.get(stackId).peek(),
                generateTag(stacksFragment.get(stackId).peek()));
    }

    private void refreshStack(int stackId, FragmentTransaction fragmentTransactionPending) {
        if (stackId == stackSelected) {
            executeFragTransaction(fragmentTransactionPending, fragmentTransaction -> attachStack(stackId, fragmentTransaction));
        }
    }

    private void clearStack(int stackId, FragmentTransaction fragmentTransactionPending) {
        Stack<IFragment> stackFragment = stacksFragment.get(stackId);
        while (stackFragment.size() > 1) {
            executeFragTransaction(fragmentTransactionPending, fragmentTransaction -> fragmentTransaction.remove(stackFragment.pop()));
        }
        refreshStack(stackId, fragmentTransactionPending);
    }

}
