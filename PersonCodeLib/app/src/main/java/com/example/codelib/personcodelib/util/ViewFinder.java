package com.example.codelib.personcodelib.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Helper for finding and tweaking a view's children
 * 
 *
 */
public class ViewFinder {
	
	/**
	 * 在View中获取View
	 * 
	 * @param pView
	 * @param pViewId
	 * @return 该View
	 */
	@SuppressWarnings("unchecked")
	public static  final <E extends View> E getView(View pView, int pViewId) {
		  return (E) pView.findViewById(pViewId);
	}

	/**
	 * 在Activity中获取View
	 * 
	 * @param pActivity
	 * @param pViewId
	 * @return 该View
	 */
	@SuppressWarnings("unchecked")
	public static final <E extends View> E  getView(Activity pActivity, int pViewId) {
		return (E) pActivity.findViewById(pViewId);
	}

	/**
	 * 在Fragment中获取View
	 * 
	 * @param pFragment
	 * @param pViewId
	 * @return 该View
	 */
	@SuppressWarnings("unchecked")
	public static final <E extends View> E getView(Fragment pFragment, int pViewId) {
		return (E) pFragment.getView().findViewById(pViewId);
	}
}
