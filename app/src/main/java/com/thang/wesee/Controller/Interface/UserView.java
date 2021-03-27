package com.thang.wesee.Controller.Interface;

public interface UserView {
    void OnValid();

    void OnLengthPass();

    void OnSamePass();

    void ValidEmailAuth();

    void OnFail();

    void OnSucess();

    void OnSucessRoom();

    void getDataRoom(String name, String date);

    void getDataType(String type);

    void getDataStatus(String call);

    void OnSucessPass();
}
