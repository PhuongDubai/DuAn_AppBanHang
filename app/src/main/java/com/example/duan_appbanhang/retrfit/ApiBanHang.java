package com.example.duan_appbanhang.retrfit;

import com.example.duan_appbanhang.mode.DonHangModel;
import com.example.duan_appbanhang.mode.MessageModel;
import com.example.duan_appbanhang.mode.LoaiSpModel;
import com.example.duan_appbanhang.mode.SanPhamMoiModel;
import com.example.duan_appbanhang.mode.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiBanHang {
  @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

  @GET("getspmoi.php")
  Observable<SanPhamMoiModel> getSpMoi();


  @POST("chitiet.php")
  @FormUrlEncoded
  Observable<SanPhamMoiModel> getSanPham(
          @Field("page") int page,
          @Field("loai") int loai
  );
  @POST("dangki.php")
  @FormUrlEncoded
  Observable<UserModel> dangKi(
          @Field("email") String email,
          @Field("pass") String pass,
          @Field("username") String username,
          @Field("mobile") String mobile,
          @Field("uid") String uid
  );


  @POST("dangnhap.php")
  @FormUrlEncoded
  Observable<UserModel> dangNhap(
          @Field("email") String email,
          @Field("pass") String pass
  );


  @POST("donhang.php")
  @FormUrlEncoded
  Observable<UserModel> createOder(
          @Field("email") String email,
          @Field("sdt") String sdt,
          @Field("tongtien") String tongtien,
          @Field("iduser") int id,
          @Field("diachi") String diachi,
          @Field("soluong") int soluong,
          @Field("chitiet") String chitiet
  );

  @POST("xemdonhang.php")
  @FormUrlEncoded
  Observable<DonHangModel> xemDonHang(
          @Field("iduser") int id

  );
  @POST("timkiem.php")
  @FormUrlEncoded
  Observable<SanPhamMoiModel> search(
          @Field("search") String search

  );
  @POST("updatetoken.php")
  @FormUrlEncoded
  Observable<MessageModel> updateToken(
          @Field("id") int id,
          @Field("token") String token


  );

  @POST("gettoken.php")
  @FormUrlEncoded
  Observable<UserModel> gettoken(
          @Field("status") int status

  );

  @POST("deleteorder.php")
  @FormUrlEncoded
  Observable<MessageModel> getdeleteOrder(
          @Field("iddonhang") int id

  );
}
