package academy.learnprogramming.db;

import academy.learnprogramming.common.Album;
import academy.learnprogramming.common.Artist;
import academy.learnprogramming.common.SongArtist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING="jdbc:sqlite:C:\\Users\\hp\\Desktop\\java-udemy\\Music\\"+DB_NAME;
    public static final String TABLE_ALBUMS="albums";
    public static final String COLUMN_ALBUM_ID="_id";
    public static final String COLUMN_ALBUM_NAME="name";
    public static final String COLUMN_ALBUM_ARTIST="artist";
    public static final int INDEX_ALBUM_ID=1;
    public static final int INDEX_ALBUM_NAME=2;
    public static final int INDEX_ALBUM_ARTIST=3;
    public static final String TABLE_ARTISTS="artists";
    public static final String COLUMN_ARTIST_ID="_id";
    public static final String COLUMN_ARTIST_NAME="name";
    public static final int INDEX_ARTIST_ID=1;
    public static final int INDEX_ARTIST_NAME=2;
    public static final String TABLE_SONGS="songs";
    public static final String COLUMN_SONG_ID="_id";
    public static final String COLUMN_SONG_TRACK="track";
    public static final String COLUMN_SONG_TITLE="title";
    public static final String COLUMN_SONG_ALBUM="album";
    public static final int INDEX_SONG_ID=1;
    public static final int INDEX_SONG_TRACK=2;
    public static final int INDEX_SONG_TITLE=3;
    public static final int INDEX_SONG_ALBUM=4;

    public static final int ORDER_BY_NONE=1;
    public static final int ORDER_BY_ASC=2;
    public static final int ORDER_BY_DESC=3;

    public static final String QUERY_ALBUMS_BY_ARTIST_START="SELECT "+TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME+" FROM "
            +TABLE_ALBUMS+" INNER JOIN "+TABLE_ARTISTS+" ON "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ARTIST+" = "
            +TABLE_ARTISTS+"."+COLUMN_ARTIST_ID+" WHERE "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME+" = \"";
    public static final String QUERY_ALBUMS_BY_ARTIST_SORT=" ORDER BY "+TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME+" COLLATE NOCASE";

    public static final String QUERY_ARTIST_FOR_SONG_START="SELECT "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME+","+TABLE_ALBUMS+
            "."+COLUMN_ALBUM_NAME+","+TABLE_SONGS+"."+COLUMN_SONG_TRACK+" FROM "+TABLE_SONGS+" INNER JOIN "+
            TABLE_ALBUMS+" ON "+TABLE_SONGS+"."+COLUMN_SONG_ALBUM+" = "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ID+" INNER JOIN "+TABLE_ARTISTS+
            " ON "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ARTIST+" = "+TABLE_ARTISTS+"."+COLUMN_ARTIST_ID+" WHERE "+COLUMN_SONG_TITLE+" =\"";
    public static final String QUERY_ARTIST_FOR_SONG_SORT=" ORDER BY "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME+","+
            TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME+" COLLATE NOCASE ";

//    create view if not exists artist_list as select artists.name,albums.name as album,
//    songs.track,songs.title from songs inner join albums on songs.album=albums._id
//    inner join artists on albums.artist=artists._id order by artists.name,
//    albums.name,songs.track

    public static final String TABLE_ARTIST_SONG_VIEW="artist_list_FINAL";
    public static final String  CREATE_ARTIST_FOR_SONG_VIEW=" CREATE VIEW IF NOT EXISTS "+
            TABLE_ARTIST_SONG_VIEW +" AS SELECT "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME+", "+TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME
            +" AS "+COLUMN_SONG_ALBUM+", "+TABLE_SONGS+"."+COLUMN_SONG_TRACK+", "+TABLE_SONGS+"."+COLUMN_SONG_TITLE+" FROM "+TABLE_SONGS+
            " INNER JOIN "+TABLE_ALBUMS+" ON "+TABLE_SONGS+"."+COLUMN_SONG_ALBUM+" = "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ID+
            " INNER JOIN "+TABLE_ARTISTS+" ON "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ARTIST+" = "+TABLE_ARTISTS+"."+COLUMN_ARTIST_ID+
            " ORDER BY "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME+","+TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME+","+
            TABLE_SONGS+"."+COLUMN_SONG_TRACK;
        //important
    public static final String QUERY_VIEW_SONG_INFO="SELECT "+COLUMN_ARTIST_NAME+","+COLUMN_SONG_ALBUM+","+COLUMN_SONG_TRACK+" FROM "+
            TABLE_ARTIST_SONG_VIEW+" WHERE "+COLUMN_SONG_TITLE+" =\"";//SUSCEPTABLE TO INJECTION ATTACKS EX :Go Your Own Way" or 1=1 or "

    public static final String QUERY_VIEW_SONG_INFO_PREP="SELECT "+COLUMN_ARTIST_NAME+", "+
            COLUMN_SONG_ALBUM+", "+COLUMN_SONG_TRACK+" FROM "+TABLE_ARTIST_SONG_VIEW+
            " WHERE "+COLUMN_SONG_TITLE+" = ?";

    //transaction
    public static final String INSERT_ARTIST=" INSERT INTO "+TABLE_ARTISTS
            +"("+COLUMN_ARTIST_NAME+") VALUES(?)";
    public static final String INSERT_ALBUMS=" INSERT INTO "+TABLE_ALBUMS+
            "("+COLUMN_ALBUM_NAME+","+COLUMN_ALBUM_ARTIST+") VALUES(?,?)";
    public static final String INSERT_SONGS="INSERT INTO "+TABLE_SONGS+"("+COLUMN_SONG_TRACK+","+COLUMN_SONG_TITLE+","+
            COLUMN_SONG_ALBUM+") VALUES(?,?,?)";
    //so that if exists we can simply go forward
    public static final String QUERY_ARTIST="SELECT "+COLUMN_ARTIST_ID+" FROM "+TABLE_ARTISTS+" WHERE "+
            COLUMN_ARTIST_NAME+" = ?";
    public static final String QUERY_ALBUM="SELECT "+COLUMN_ALBUM_ID+" FROM "+TABLE_ALBUMS+" WHERE "+
            COLUMN_ALBUM_NAME+" = ?";

    public static final String QUERY_ALBUMS_BY_ARTIST_ID="SELECT * FROM "+TABLE_ALBUMS+" WHERE "+COLUMN_ALBUM_ARTIST+
            " = ? ORDER BY "+COLUMN_ALBUM_NAME+" COLLATE NOCASE";

    public static final String UPDATE_ARTIST_NAME="UPDATE "+TABLE_ARTISTS+" SET "+
            COLUMN_ARTIST_NAME+"= ? WHERE "+COLUMN_ARTIST_ID+" = ?";
    private Connection conn;
    private PreparedStatement querySongInfoView;
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;
    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;
    private PreparedStatement queryAlbumByArtistId;
    private PreparedStatement updateArtistName;

    private static Datasource instance=new Datasource();

    private Datasource(){

    }
    public static Datasource getInstance(){
        return instance;
    }
    public boolean open(){
        try{
            conn= DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists=conn.prepareStatement(INSERT_ARTIST,Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums=conn.prepareStatement(INSERT_ALBUMS,Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs=conn.prepareStatement(INSERT_SONGS);
            queryArtist=conn.prepareStatement(QUERY_ARTIST);
            queryAlbum=conn.prepareStatement(QUERY_ALBUM);
            queryAlbumByArtistId=conn.prepareStatement(QUERY_ALBUMS_BY_ARTIST_ID);
            updateArtistName=conn.prepareStatement(UPDATE_ARTIST_NAME);
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Couldn't connect to database "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public void close(){
        try{
            if(querySongInfoView!=null)
            {
                querySongInfoView.close();
            }
            if(insertIntoArtists!=null)
            {
                insertIntoArtists.close();
            }
            if(insertIntoAlbums!=null)
            {
                insertIntoAlbums.close();
            }
            if(insertIntoSongs!=null)
            {
                insertIntoSongs.close();
            }
            if(queryArtist!=null)
            {
                queryArtist.close();
            }
            if(queryAlbum!=null)
            {
                queryAlbum.close();
            }
            if(queryAlbumByArtistId!=null)
            {
                queryAlbumByArtistId.close();
            }
            if(updateArtistName!=null)
            {
                updateArtistName.close();
            }
            if(conn!=null)
            {
                conn.close();
            }
        }
        catch (SQLException e)
        {
            System.out.println("Couldn't connect to database "+e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Artist> queryArtist(int sortOrder)
    {
        StringBuilder sb=new StringBuilder("select * from ");
        sb.append(TABLE_ARTISTS);
        if(sortOrder!=ORDER_BY_NONE)
        {
            sb.append(" order by ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" collate nocase ");
            if(sortOrder==ORDER_BY_DESC)
            {
                sb.append("desc");
            }
            else{
                sb.append("asc");
            }
        }
        try(Statement statement=conn.createStatement();
            ResultSet results=statement.executeQuery(sb.toString())){
            List<Artist> artists=new ArrayList<>();
            while (results.next())
            {
                try{
                    Thread.sleep(20);
                }
                catch (InterruptedException e)
                {

                }
                Artist artist=new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));//by column index
                artist.setName(results.getString(INDEX_ARTIST_NAME));//by column index
//                artist.setId(results.getInt(COLUMN_ARTIST_ID));//by column name
//                artist.setName(results.getString(COLUMN_ARTIST_NAME));//by column name
                artists.add(artist);
            }
            return artists;
        }
        catch (SQLException e) {
            System.out.println("Query Failed" + e.getMessage());
            e.printStackTrace();
            return null;
        }
//        Statement statement=null;
//        ResultSet results=null;
//    try{
//            statement=conn.createStatement();
//            results=statement.executeQuery("select * from "+TABLE_ARTISTS);
//            List<Artist> artists=new ArrayList<>();
//            while (results.next())
//            {
//                Artist artist=new Artist();
//                artist.setId(results.getInt(COLUMN_ARTIST_ID));
//                artist.setName(results.getString(COLUMN_ARTIST_NAME));
//                artists.add(artist);
//            }
//            return artists;
//        }
//        catch (SQLException e)
//        {
//            System.out.println("Query Failed"+e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//        finally {
//            try{
//                if(results!=null)
//                {
//                    results.close();
//                }
//            }
//            catch (SQLException e)
//            {
//                System.out.println("Error closings ResultSet");
//            }
//            try{
//                if (statement!=null)
//                {
//                    statement.close();
//                }
//            }
//            catch (SQLException e)
//            {
//                System.out.println("Error closing statement");
//            }
//        }
    }

    public List<Album> queryAlbumForArtistId(int id){
        try{
            queryAlbumByArtistId.setInt(1,id);
            ResultSet results=queryAlbumByArtistId.executeQuery();
            List<Album> albums=new ArrayList<>();
            while (results.next())
            {
                Album album = new Album();
                album.setId(results.getInt(1));
                album.setName(results.getString(2));
                album.setArtistid(id);
                albums.add(album);
            }
            return albums;
        }
        catch (SQLException e)
        {
            System.out.println("Query Failed : "+e.getMessage());
            return null;
        }
    }
    public List<String> queryAlbumsForArtist(String artistName,int sortOrder)
    {
        StringBuilder sb=new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");

//        sb.append("SELECT ");//delete
//        sb.append(TABLE_ALBUMS);
//        sb.append('.');
//        sb.append(COLUMN_ALBUM_NAME);
//        sb.append(" FROM ");
//        sb.append(TABLE_ALBUMS);
//        sb.append(" INNER JOIN ");
//        sb.append(TABLE_ARTISTS);
//        sb.append(" ON ");
//        sb.append(TABLE_ALBUMS);
//        sb.append('.');
//        sb.append(COLUMN_ALBUM_ARTIST);
//        sb.append(" = ");
//        sb.append(TABLE_ARTISTS);
//        sb.append('.');
//        sb.append(COLUMN_ARTIST_ID);
//        sb.append(" where ");
//        sb.append(TABLE_ARTISTS);
//        sb.append('.');
//        sb.append(COLUMN_ARTIST_NAME);
//        sb.append(" = \"");
//        sb.append(artistName);
//        sb.append("\"");
        if(sortOrder!=ORDER_BY_NONE)
        {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
//            sb.append(" ORDER BY ");
//            sb.append(TABLE_ALBUMS);
//            sb.append(".");
//            sb.append(COLUMN_ALBUM_NAME);
//            sb.append(" COLLATE NOCASE ");
            if(sortOrder==ORDER_BY_DESC)
            {
                sb.append(" DESC ");
            }
            else
            {
                sb.append(" ASC ");
            }
        }
        System.out.println("Sql statement = "+sb.toString());
        try(Statement statement=conn.createStatement();
            ResultSet results = statement.executeQuery(sb.toString()))
        {
            List<String> albums=new ArrayList<>();
            while(results.next())
            {
                albums.add(results.getString(1));
            }
            return albums;
        }
        catch (SQLException e)
        {
            System.out.println("Query failed :"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public List<SongArtist> queryArtistForSong(String songName,int sortOrder)
    {
        StringBuilder sb=new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");
        if(sortOrder!=ORDER_BY_NONE)
        {
            sb.append(QUERY_ARTIST_FOR_SONG_SORT);
            if(sortOrder == ORDER_BY_DESC)
            {
                sb.append(" DESC ");

            }
            else {
                sb.append(" ASC");
            }
        }
        System.out.println("SQL STATEMENT = " +sb.toString());
        try(Statement statement=conn.createStatement();
            ResultSet results=statement.executeQuery(sb.toString()))
        {
            List<SongArtist> songArtists=new ArrayList<>();
            while (results.next())
            {
                SongArtist songArtist=new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;
        }
        catch (SQLException e)
        {
            System.out.println("Query Failed :"+e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
    public void querySongsMetadata()
    {
        String sql = "SELECT * FROM "+TABLE_SONGS;
        try(Statement statement=conn.createStatement();
            ResultSet results=statement.executeQuery(sql))
        {
            ResultSetMetaData meta=results.getMetaData();
            int numColumns=meta.getColumnCount();
            for(int i=1;i<=numColumns;i++)
            {
                System.out.format("Column %d in the songs table is names %s\n",i,meta.getColumnName(i));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Query Failed "+e.getMessage());
            e.printStackTrace();
        }
    }
    public int getCount(String table)
    {
        String sql = "SELECT COUNT(*) as count FROM "+table;
        try(Statement statement=conn.createStatement();
        ResultSet results=statement.executeQuery(sql))
        {
            int count = results.getInt("count");
            return count;
        }
        catch (SQLException e)
        {
            System.out.println("Query Failed: "+e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    public boolean createViewForSongArtist(){
        try(Statement statement=conn.createStatement())
        {
            System.out.println(CREATE_ARTIST_FOR_SONG_VIEW);
            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);

            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Query Failed: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    //select name,album,track from artist_list where title="title"
    public List<SongArtist> querySongInfoView(String title)
    {

        try{
            querySongInfoView.setString(1,title);
            System.out.println(querySongInfoView);

            ResultSet results=querySongInfoView.executeQuery();

            List<SongArtist> songArtists = new ArrayList<>();
            while (results.next())
            {
                SongArtist songArtist=new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);

            }
            return songArtists;
        }
        catch (SQLException e)
        {
            System.out.println("Query failed :"+e.getMessage());
            e.printStackTrace();
            return null;
        }
//        StringBuilder sb=new StringBuilder(QUERY_VIEW_SONG_INFO);
//        sb.append(title);
//        sb.append("\"");
//        System.out.println(sb.toString());
//        try(Statement statement=conn.createStatement();
//            ResultSet results=statement.executeQuery(sb.toString()))
//        {
//            List<SongArtist> songArtists = new ArrayList<>();
//            while (results.next())
//            {
//                SongArtist songArtist=new SongArtist();
//                songArtist.setArtistName(results.getString(1));
//                songArtist.setAlbumName(results.getString(2));
//                songArtist.setTrack(results.getInt(3));
//                songArtists.add(songArtist);
//
//            }
//            return songArtists;
//        }
//        catch (SQLException e)
//        {
//            System.out.println("Query failed :"+e.getMessage());
//            e.printStackTrace();
//            return null;
//        }

    }
    private int insertArtist(String name) throws SQLException {
        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            insertIntoArtists.setString(1, name);
            int affectedRows = insertIntoArtists.executeUpdate();//returns no of affected rows
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert artists ");
            }
            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }
    private int insertAlbum(String name,int artistId) throws SQLException
    {
        queryAlbum.setString(1,name);
        ResultSet results=queryAlbum.executeQuery();
        if(results.next())
        {
            return results.getInt(1);
        }
        else
        {
            insertIntoAlbums.setString(1,name);
            insertIntoAlbums.setInt(2,artistId);

            int affectedRows=insertIntoAlbums.executeUpdate();//returns no of affected rows
            if(affectedRows!=1)
            {
                throw new SQLException("Couldn't insert albums ");//we didn't implement try catch because we want to raise the exception
                //because thge transaction will be rolled back in the insertSong
            }
            ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if(generatedKeys.next())
            {
                return generatedKeys.getInt(1);
            }
            else
            {
                throw new SQLException("Couldn't get _id for album");//shouldn't handle because the transaction will be rolled back in insertintosongs..
            }
        }
    }
    public boolean updateArtistName(int id,String newName)
    {
        try{
            updateArtistName.setString(1,newName);
            updateArtistName.setInt(2,id);
            int affectedRecords=updateArtistName.executeUpdate();
            return affectedRecords==1;
        }
        catch (SQLException e)
        {
            System.out.println("Update Failed "+e.getMessage());
            return false;
        }
    }
    public void  insertSong(String title,String artist,String album,int track) throws SQLException
    {
        try{
            conn.setAutoCommit(false);

            int artistId = insertArtist(artist);
            int albumId = insertAlbum(album,artistId);
            insertIntoSongs.setInt(1,track);
            insertIntoSongs.setString(2,title);
//            insertIntoSongs.setInt(8,albumId);// for error
            insertIntoSongs.setInt(3,albumId);


            int affectedRows=insertIntoSongs.executeUpdate();//returns no of affected rows
            if(affectedRows==1)
            {
                conn.commit();//not necessary i think
            }
            else
            {
                throw new SQLException("Couldn't get _id for album");
            }

        }
//        catch (SQLException e1)//only sql Exception will be caught that's not what we want
        catch (Exception e1)
        {
            System.out.println("Insert Song Exceptions : "+e1.getMessage());
            try{
                System.out.println("Performing rollback ");
                conn.rollback();
            }
            catch (SQLException e2)
            {
                System.out.println("the things are really bad "+e2.getMessage());

            }
        }
        finally {
            try{
                System.out.println("Resetting default commit behaviour ");
                conn.setAutoCommit(true);
            }
            catch (SQLException e)
            {
                System.out.println("Couldn't reset auto-commit "+e.getMessage());

            }
        }

    }


}




















































