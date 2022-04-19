package com.mycompany.jukebox1;

//import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
//import org.junit.jupiter.api.Test;
class TestingSong {

	Song song=new Song();
       //@Test
	void testSearchByArtist() throws Exception {
		List<Song> list=new ArrayList<Song>();
		Connection conn = JDBC.getConnection();
		list=song.storeSongsInArrayList(conn);
		List<Song> list2=song.searchByArtist(list, "AP");	//Searching for Artist name
		assertTrue(list2.size()>0);
	}
	//@Test
	void testSearchByGenre() throws Exception {
		List<Song> list=new ArrayList<Song>();
		Connection conn = JDBC.getConnection();
		list=song.storeSongsInArrayList(conn);
		List<Song> list2=song.searchByGenre(list, "pop");		//Searching for Genre
		assertTrue(list2.size()>0);
	}
	//@Test
	void testSearchByAlbum() throws Exception {
		List<Song> list=new ArrayList<Song>();
		Connection conn = JDBC.getConnection();
		list=song.storeSongsInArrayList(conn);
		List<Song> list2=song.searchByAlbum(list, "unknown");	//Searching for Album
		assertTrue(list2.size()>0);
	}

    private void assertTrue(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
