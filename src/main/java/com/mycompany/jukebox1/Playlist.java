package com.mycompany.jukebox1;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Playlist {
	public String playlistIdCreation(Connection conn) throws SQLException{
		String playlistId="";
		String newPlaylistId="";
		Statement statement=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=statement.executeQuery("select PlayListId from userplaylist order by PlayListId");
		while(rs.next()) {	
				playlistId=rs.getString(1);	
		}
	
		newPlaylistId="PLY"+Integer.toString(Integer.parseInt(playlistId.substring(3,4))+1);			
		return newPlaylistId;
	}
	
	public void createUserPlaylist(Connection conn, String username) {
		try {
			String playlistID=playlistIdCreation(conn);
			PreparedStatement psmt=conn.prepareStatement("insert into userplaylist values(?,?,?)");
			psmt.setString(1, playlistID);
			System.out.print("\n\t\tEnter Name: ");
			String playlistName=new Scanner(System.in).next();
			psmt.setString(2, playlistName);
			psmt.setString(3, username);
			int row=psmt.executeUpdate();
			psmt.close();
			if(row>0) {
				System.out.print("\n\t\tPlaylist successfully created.");
				insertSongsIntoPlayList(playlistID,conn);
			}
			else {
				System.out.print("\n\t\tError");
			}
		}
		catch(Exception e) {
			System.out.print("\n\t\tCannot Create Playlist");
		}
	}
	
	public void insertSongsIntoPlayList(String playlistID, Connection conn) {
		Scanner scanObj=new Scanner(System.in);
		try {
			while(true){
				System.out.println("\n\n\t\tAdd\n\t\t1. Song\n\t\t2. Podcast\n\t\t3. Exit");
				int choice=scanObj.nextInt();
				if(choice==1) {
					PreparedStatement psmt=conn.prepareStatement("Insert into playlist (PlayListId,SongId,PodId) values(?,?,?)");
					psmt.setString(1, playlistID);
					System.out.print("\n\t\tSongID: ");
					String songID=scanObj.next();
					psmt.setString(2, songID);
					psmt.setString(3, null);
					int row=psmt.executeUpdate();
					if(row>0) {
						System.out.print("\n\n\t\tSong added.");
					}
					else {
						System.out.print("\n\n\t\tUnable to add songs at this moment");
					}
				}
				else if(choice==2) {
					System.out.print("\n\t\tPodcast ID: ");
					String podcastID=scanObj.next();
				
					Statement statementObj=conn.createStatement();
					ResultSet rs=statementObj.executeQuery("select SongId from podcastsonglist where PodId='"+podcastID+"'");
					int row = 0;
					while(rs.next()) {
			
					
					PreparedStatement psmt=conn.prepareStatement("Insert into playlist (PlayListId,SongId,PodId) values(?,?,?)");
					psmt.setString(1, playlistID);
					psmt.setString(2, rs.getString(1));
					psmt.setString(3, podcastID);
					row=psmt.executeUpdate();
					}
					if(row>0) {
						System.out.print("\n\n\t\tPodcast added.");
					}
					else {
						System.out.print("\n\n\t\tUnable to add songs at this moment");
					}
				}
				else if(choice==3) {
					break;   
				}
				else {
					System.out.print("\n\t\t Y or N");
				}
			}
		}
		catch(Exception e) {
			System.out.print("\n\t\tInvalid song code");
		}
	}
	
	public List<Song> viewSongsInPlaylist(Connection conn, String username) {
		ArrayList<Song> songList=new ArrayList<Song>();
		try {
			Statement statementObj=conn.createStatement();
			ResultSet rs=statementObj.executeQuery("select playlistid,playlistname from userplaylist where username='"+username+"'");
			int count=0;
			while(rs.next()) {
				System.out.print("\n\t\t Playlist ID= "+rs.getString(1)+", Name= "+rs.getString(2));
				count++;
			}
			if(count!=0) {
				System.out.print("\n\n\t\tEnter Playlist ID: ");
				String playlistId=new Scanner(System.in).next();
				ResultSet rs2=statementObj.executeQuery("select * from song where songId in(select songId from PlayList  where PlaylistId  in (select playlistId from userplaylist where username='"+username+"' AND playlistId='"+playlistId+"'))");
				
				
				while(rs2.next()) {
					songList.add(new Song(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5),rs2.getString(6)));
				}
				System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
				System.out.println("\n************************************************************************************************************");
				songList.forEach(s->{
					System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());
				});
				playSongsPlaylist(songList);
			}
			if(count==0) {
				System.out.print("Create a playlist before trying again");
				createUserPlaylist(conn,username);
			}
			
		}
		catch(SQLException se) {
			System.out.print("\n\t\tUnable to fetch songs at this moment.");
		}
		return songList;
	}
	public void playSongsPlaylist(List<Song> songList) {
		try {
			for(int i=0;i<songList.size();i++) {
			
				String url="F:\\songs\\"+songList.get(i).getSongID()+".wav";
				Clip clip=AudioSystem.getClip();
				File f=new File(url);
			
				AudioInputStream inputStream=AudioSystem.getAudioInputStream(f.getAbsoluteFile());
				
				clip.open(inputStream);
				clip.loop(0);
				clip.start();
				
				System.out.print("\n\t\t"+songList.get(i).getSongName()+" getting played");
				System.out.print("\n\t\t1. Next");
				System.out.print("\t\t2. Prev");
				System.out.print("\t\t3. Restart");
				System.out.print("\t\t4. Stop");

				int choice=new Scanner(System.in).nextInt();
				while(true) {
					if(clip.isActive()) {
						
				
						if(choice==1) {
							clip.close();
							clip.stop();
							break;
						} else if(choice==2) {
							clip.close();
							clip.stop();
							i=i-2;
							break;
						} else if(choice==3) {
							clip.close();
							clip.stop();
							i=i-1;
							break;
						} else if(choice==4) {
							break;
						}
						
					}
					else {
						clip.close();
						clip.stop();
						break;
					}
				}
				if(choice==4) {
					clip.close();
					clip.stop();
					System.out.print("\n\t\tDo you wish to continue?(Y/N) ");
					String listen=new Scanner(System.in).next();
					if(listen.equalsIgnoreCase("Y")) {
						Main main=new Main();
						main.songMain();
						break;
					}
					else if(listen.equalsIgnoreCase("N")) {
						System.out.print("\n\t\tThanks for using Jukebox. See you soon.");
						System.exit(0);
					}
					
				}
			}
		}
		catch(Exception e) {
			System.out.print("\n\t\tJukebox stopped");
		}
	}
}


