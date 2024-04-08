package desebastian_csc340assignment4;

import java.io.File;

/**
 * @author Sebastian Del Campo
 * CSC 340 - Assignment 4
 * 
 * Basic structure for keeping track of music and metadata
 * @since April 8, 2024
 */

public class Music 
{
    //Private Data
    private String title;
    private File musicFile;
    private String description;
    
    //Constructors
    public Music(String title, File musicFile, String description)
    {
        this.title = title;
        this.musicFile = musicFile;
        this.description = description;
    }

    //Getters and Setters
    public String getTitle() {
        return title;
    }

    public File getMusicFile() {
        return musicFile;
    }

    public String getDescription() {
        return description;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }    
    
    public void setMusicFile(File musicFile) {
        this.musicFile = musicFile;
    }   

    public void setDescription(String description) {
        this.description = description;
    }    
}
