package br.com.washington.androidprojfiap.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by washington on 09/09/2017.
 */

public class Dog implements Parcelable{

    private static final long serialVersionUID = 6601006766832473959L;
    public long id;
    public String tipo;
    public String nome;
    public String desc;
    public String urlFoto;
    public String urlInfo;
    public String urlVideo;
    public String latitude;
    public String longitude;
    public static final Parcelable.Creator<Dog> CREATOR = new Parcelable.Creator<Dog>() {
        @Override
        public Dog createFromParcel(Parcel p) {
            Dog c = new Dog();
            c.readFromParcel(p);
            return c;
        }

        @Override
        public Dog[] newArray(int size) {
            return new Dog[size];
        }
    };
    public boolean selected; // Flag para indicar que o dog está selecionado

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Escreve os dados para serem serializados.
        dest.writeLong(id);
        dest.writeString(this.tipo);
        dest.writeString(this.nome);
        dest.writeString(this.desc);
        dest.writeString(this.urlFoto);
        dest.writeString(this.urlInfo);
        dest.writeString(this.urlVideo);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }

    public void readFromParcel(Parcel parcel) {
        // Lê os dados na mesma ordem que foram escritos
        this.id = parcel.readLong();
        this.tipo = parcel.readString();
        this.nome = parcel.readString();
        this.desc = parcel.readString();
        this.urlFoto = parcel.readString();
        this.urlInfo = parcel.readString();
        this.urlVideo = parcel.readString();
        this.latitude = parcel.readString();
        this.longitude = parcel.readString();
    }

    @Override
    public String toString() {
        return "Dog{" + "nome='" + nome + '\'' + '}';
    }
}
