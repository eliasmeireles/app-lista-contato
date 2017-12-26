package systemplus.com.br.listadecontatos.model;

import java.io.Serializable;

/**
 * Created by elias on 20/12/17.
 */

public class Endereco implements Serializable{

    private long id;
    private String enderecoInfor;
    private Double latitude;
    private Double longitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnderecoInfor() {
        return enderecoInfor;
    }

    public void setEnderecoInfor(String enderecoInfor) {
        this.enderecoInfor = enderecoInfor;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "id='" + id + '\'' +
                ", enderecoInfor='" + enderecoInfor + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

