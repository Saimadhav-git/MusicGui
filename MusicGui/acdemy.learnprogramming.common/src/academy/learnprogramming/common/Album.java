package academy.learnprogramming.common;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Album {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty artistid;

    public Album()
    {
        this.id=new SimpleIntegerProperty();
        this.name=new SimpleStringProperty();
        this.artistid=new SimpleIntegerProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getArtistid() {
        return artistid.get();
    }

    public void setArtistid(int artistid) {
        this.artistid.set(artistid);
    }
}
