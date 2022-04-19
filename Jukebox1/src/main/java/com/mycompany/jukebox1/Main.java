package com.mycompany.jukebox1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] arg)  
        {
		
		try {	
			
		System.out.println("\t\t*****************************************************************");
		System.out.println("\t\t*                   WELCOME TO THE JUKEBOX                      *");
		System.out.println("\t\t*****************************************************************");
		Scanner scanObj=new Scanner(System.in);
                Connection conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/Jukebox1","root","1321@asdfgf");
			
			
                     
			System.out.println("check1");
                       //new Song().displaySongs(conn);
		        User user=new User();
                     //Connection conn = JDBC.getConnection();
                    Class.forName("com.mysql.cj.jdbc.Driver");
			while(true){
				System.out.println("\n\t\t\t1.Sign In");
				System.out.println("\n\t\t\t2.Sign UP");
				int choice=scanObj.nextInt();
				if(choice==1)
                                {
					user.createUser(conn);
					
				}
				else if(choice==2) 
                                {
					Scanner s=new Scanner(System.in);
					System.out.println("\n\t\tEnter your username: ");
					String username=s.next();
					System.out.println("\n\t\tEnter your password: ");
					String password=s.next();
                                        System.out.println("create sucessfully");
                                        String create =s.next();
					
					boolean validation=user.checkUsername(conn, username, password);
					if(validation) 
                                        {
						Main main=new Main();
						main.songMainForPlaylist(username);
					}
					else 
                                        {
						System.out.println("Incorrect username/password");
					}
					break;
				}
				else
                                {
					System.out.print("\nInvalid choice. Please select from 1 or 2.");
				}
			}
                      System.out.print("\n\t\tTHANK YOU SEE YOU AGAIN.");
		
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void songMainForPlaylist(String username) {
		try {
			Connection conn = JDBC.getConnection();
			Song song=new Song();
			Podcast podcast=new Podcast();
			Playlist playlist=new Playlist();
			System.out.println();
			while(true){
			System.out.println("\n\t\tEnter:\n\t\t1. All Songs\n\t\t2. Search\n\t\t3. Podcast Display\n\t\t4. Podcast Search\n\t\t5. Playlist Creation\n\t\t6. View Playlist");
			int choice=new Scanner(System.in).nextInt();
			switch(choice) {
				case 1:
					song.displaySongs(conn);
					song.playSongs();
				break;
				case 2:
					List<Song> songList=new ArrayList<Song>();
					songList=song.storeSongsInArrayList(conn);
					song.searchSongs(songList);
				break;
				case 3:
					podcast.displayPodcast(conn);
				break;
				case 4:
					podcast.searchPodcast(conn);
				break;
				case 5:
					playlist.createUserPlaylist(conn, username);
				break;
				case 6:
					playlist.viewSongsInPlaylist(conn, username);
				break;
				default:
					System.out.println("\n\t\tInvalid Choice");
			}
			}
		
                }
                catch(Exception e) {
			e.getMessage();
		}
	}
        
	public void songMain() {
		try {
			Connection conn = JDBC.getConnection();
			Song song=new Song();
			Podcast podcast=new Podcast();
			System.out.println();
			while(true) {
			System.out.println("\n\t\tEnter:\n\t\t1. All Songs\n\t\t2. Search\n\t\t3. Podcast Display\n\t\t4. Podcast Search\n\t\t");
			int choice=new Scanner(System.in).nextInt();
			switch(choice) {
				case 1:
					song.displaySongs(conn);
					song.playSongs();
				break;
				case 2:
					List<Song> songList=new ArrayList<Song>();
					songList=song.storeSongsInArrayList(conn);
					song.searchSongs(songList);
				break;
				case 3:
					podcast.displayPodcast(conn);
				break;
				case 4:
					podcast.searchPodcast(conn);
				break;
				default:
					System.out.println("\n\t\tInvalid Choice");
			}
			}
		}
		catch(Exception e) {
			e.getMessage();
		}
	}

    private static class main {

        private static void songMainForPlaylist(String username) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public main() {
        }
    }
}




