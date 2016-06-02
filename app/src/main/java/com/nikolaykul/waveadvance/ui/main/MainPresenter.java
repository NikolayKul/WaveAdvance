package com.nikolaykul.waveadvance.ui.main;

import android.util.Pair;

import com.nikolaykul.waveadvance.data.MathManager;
import com.nikolaykul.waveadvance.di.scope.PerActivity;
import com.nikolaykul.waveadvance.ui.base.Presenter;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@PerActivity
public class MainPresenter extends Presenter<MainMvpView> {
    private final MathManager mManager;

    @Inject public MainPresenter(MathManager manager) {
        this.mManager = manager;
    }

    @Override
    public void init() {
        Timber.i("MainPresenter was initialized.");
    }

    @Override
    public void destroy() {
        Timber.i("MainPresenter was destroyed.");
    }

    public void computeNewCoordinate(Pair<Float, Float> coordinate) {
        final Observable<Double> obsX =
                Observable.defer(() -> Observable.just(functionX(coordinate)));
        final Observable<Double> obsY =
                Observable.defer(() -> Observable.just(functionY(coordinate)));
        Observable.combineLatest(obsX, obsY, Pair::new)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> getMvpView().showLoading())
                .doAfterTerminate(() -> getMvpView().hideLoading())
                .subscribe(getMvpView()::showNewCoordinate);
    }

    private double functionX(Pair<Float, Float> pair) {
        return mManager.u(pair.first, pair.second);
    }

    private double functionY(Pair<Float, Float> pair) {
        return mManager.v(pair.first, pair.second);
    }

}