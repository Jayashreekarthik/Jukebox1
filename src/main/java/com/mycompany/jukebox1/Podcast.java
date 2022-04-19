package com.mycompany.jukebox1;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Podcast {
	
		String podId;
		String artist;
		String genre;
		String dateOfPodcast;
               
		public String getPodId() 
                {
			return podId;
		}
		public void setPodId(String podId) {
			this.podId = podId;
		}
		public String getArtist() {
			return artist;
		}
		public void setArtist(String artist) {
			this.artist = artist;
		}
		public String getGenre() {
			return genre;
		}
		public void setGenre(String genre) {
			this.genre = genre;
		}
		public String getDateOfPodcast() {
			return dateOfPodcast;
		}
		public void setDateOfPodcast(String dateOfPodcast) {
			this.dateOfPodcast = dateOfPodcast;
		}
		public Podcast() {
			
		}
		public Podcast(String podId, String artist, String genre, String dateOfPodcast) {
			super();
			this.podId = podId;
			this.artist = artist;
			this.genre = genre;
			this.dateOfPodcast = dateOfPodcast;
		}
	
		public List<Podcast> storePodcastInArray(Connection conn) throws SQLException{
			List<Podcast> podcastList=new ArrayList<Podcast>();
			
			Statement statementObj=conn.createStatement();
			ResultSet rs=statementObj.executeQuery("select * from podcast");
			
			while(rs.next()) {
				podcastList.add(new Podcast(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
			}
			
			
			return podcastList;
		}
		
		
		public void displayPodcast(Connection conn) throws SQLException {
			try {
                            //new Podcast().displayPodcast(conn);
			List<Podcast> podcastList=new ArrayList<Podcast>();
			podcastList=storePodcastInArray(conn);
			System.out.printf("\n\t\t%-10s %-20s %-20s %-20s","Podcast ID","Artist","Genre","Date");
			System.out.println("\n\t\t************************************************************************************************************");
			podcastList.forEach(p->{System.out.printf("\t\t%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getArtist(),p.getGenre(),p.getDateOfPodcast());});
			
			System.out.print("\nPodcastId=");
			String podcastID=new Scanner(System.in).next();
			Statement statementObj;
			
			statementObj = conn.createStatement();
			
			ResultSet rs2=statementObj.executeQuery("select * from song where songId in(select songId from podcastsonglist where PodId='"+podcastID+"')");
			ArrayList<Song> songList=new ArrayList<Song>();
			while(rs2.next()) {
				songList.add(new Song(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5),rs2.getString(6)));
			}
			System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
			System.out.println("\n************************************************************************************************************");
			songList.forEach(s->{System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());});
			} catch (SQLException e) {
				System.out.print("\n\t\tJukebox Not Responding");
			}
		
		}
		
		public void searchPodcast(Connection conn) throws SQLException  {
			Scanner scanObj=new Scanner(System.in);
			List<Podcast> podcastList=new ArrayList<Podcast>();
			podcastList=storePodcastInArray(conn);
			Song song=new Song();
				try {
				System.out.println("\n\t\tEnter your choice to search from Podcast:\n\t\t1. Artist\n\t\t2. Genre\n\t\t3. Date of Podcast");
				int choice=scanObj.nextInt();
				switch(choice) {
				case 1:
					System.out.print("\n\t\tEnter Artist: ");
					String Artist=scanObj.next();
					System.out.printf("%-10s %-21s %-14s %-15s\n","PodId","Artist","Genre","DateOfPodcast");
					System.out.println("*************************************************************");
					podcastList.stream().filter(s->s.getArtist().contains(Artist)).forEach(p->{
						System.out.printf("%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getArtist(),p.getGenre(),p.getDateOfPodcast());
					});
					System.out.print("\nPodcastId=");
					String podcastID=new Scanner(System.in).next();
					Statement statementObj;
					
					statementObj = conn.createStatement();
					
					ResultSet rs2=statementObj.executeQuery("select * from song where songId in(select songId from podcastsonglist where PodId='"+podcastID+"')");
					ArrayList<Song> songList=new ArrayList<Song>();
					while(rs2.next()) {
						songList.add(new Song(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5),rs2.getString(6)));
					}
					System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
					System.out.println("\n************************************************************************************************************");
					songList.forEach(s->{System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());});
					song.playSongs();
					break;
				case 2:
					System.out.print("\n\t\tEnter Genre: ");
					String genre=scanObj.next();
					System.out.printf("%-10s %-21s %-14s %-15s\n","PodId","Artist","Genre","DateOfPodcast");
					System.out.println("*************************************************************");
					podcastList.stream().filter(s->s.getGenre().contains(genre)).forEach(p->{
						System.out.printf("%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getArtist(),p.getGenre(),p.getDateOfPodcast());
					});
					System.out.print("\nPodcastId=");
					String podcastID1=new Scanner(System.in).next();
					Statement statementObj1;
					
					statementObj1 = conn.createStatement();
					
					ResultSet rs3=statementObj1.executeQuery("select * from song where songId in(select songId from podcastsonglist where PodId='"+podcastID1+"')");
					ArrayList<Song> songList1=new ArrayList<Song>();
					while(rs3.next()) {
						songList1.add(new Song(rs3.getString(1),rs3.getString(2),rs3.getString(3),rs3.getString(4),rs3.getString(5),rs3.getString(6)));
					}
					System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
					System.out.println("\n************************************************************************************************************");
					songList1.forEach(s->{System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());});
					song.playSongs();
					break;
				case 3:
					System.out.print("\n\t\tEnter Date:(YYYY-MM-DD) ");
					String date=scanObj.next();
					System.out.printf("%-10s %-21s %-14s %-15s\n","PodId","Artist","Genre","DateOfPodcast");
					System.out.println("*************************************************************");
					podcastList.stream().filter(s->s.getDateOfPodcast().contains(date)).forEach(p->{
						System.out.printf("%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getArtist(),p.getGenre(),p.getDateOfPodcast());
					});
					System.out.print("\nPodcastId=");
					String podcastID3=new Scanner(System.in).next();
					Statement statementObj3;
					
					statementObj3 = conn.createStatement();
					
					ResultSet rs4=statementObj3.executeQuery("select * from song where songId in(select songId from podcastsonglist where PodId='"+podcastID3+"')");
					ArrayList<Song> songList3=new ArrayList<Song>();
					while(rs4.next()) {
						songList3.add(new Song(rs4.getString(1),rs4.getString(2),rs4.getString(3),rs4.getString(4),rs4.getString(5),rs4.getString(6)));
					}
					System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
					System.out.println("\n************************************************************************************************************");
					songList3.forEach(s->{System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());});
					song.playSongs();
				break; 
				default:
					System.out.print("\n\t\tInvalid choice");
				}
			} catch (Exception e) {
				System.out.print("\n\t\tJukebox Not Responding");
			}
		}
}


    

