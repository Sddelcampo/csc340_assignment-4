package desebastian_csc340assignment4;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

/**
 * @author Sebastian Del Campo
 * CSC 340 - Assignment 4
 * 
 * CRUD Application testing out adding, editing, and deleting music files and 
 * keeping track of information regarding the music
 * @since April 8, 2024
 */

public class Main 
{
    //Database storage through ArrayList
    static ArrayList<Music> musicDatabase = new ArrayList<>();

    //Basic UI for user interaction on CRUD application
    static void mainMenu()
    {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\nWelcome to the Music Uploader 2004!");
            System.out.println("Please enter a following option:");            
            System.out.println("1) Add Music");
            System.out.println("2) Edit Music");
            System.out.println("3) Delete Music");
            System.out.println("4) Quit");
        
            option = scanner.nextInt();

            switch (option) 
            {
                case 1:
                    //Adds music to database
                    addMusic();
                    break;
                case 2:
                    //Edit music in database
                    editMusic();
                    break;
                case 3:
                    //Deletes music in database
                    deleteMusic();
                    break;
                case 4:
                    //Ends the program
                    System.out.println("\nThank you for using Music Uploader 2004! Please come again soon!");
                    break;
                default:
                    System.out.println("\nThat option is unavailble, please enter a value based on the menu.\n");
            }
        } while (option != 4);

        scanner.close();
    }
    
    //Function that adds the audio file into an audio folder and also keeps track of music record through a text file
    static void addMusic()
    {
        Scanner scanner = new Scanner(System.in);

        //Prompt the user to enter music information
        String title;
        String filePath;
        String description;
        String addMore = null;
        do 
        {
            System.out.println("\nEnter the title for your music: ");
            title = scanner.nextLine();

            System.out.println("\nEnter the absolute path for your music: ");
            filePath = scanner.nextLine();
            
            while (!doesFileExist(filePath)) 
            {
                System.out.println("Invalid file path. Please enter a valid file path.\n");
                System.out.println("Enter the path for your music: ");
                filePath = scanner.nextLine();
            }
            
            System.out.println("\nEnter a description for your music: ");
            description = scanner.nextLine();

            //Create a Music object with the entered information and add it to the ArrayList
            musicDatabase.add(new Music(title, new File(filePath), description));

            System.out.println("\nDo you want to add more music? (Y/N): ");
            addMore = scanner.nextLine();
            
            while(!addMore.equalsIgnoreCase("Y") && !addMore.equalsIgnoreCase("N"))
            {
                System.out.println("Invalid Option. Try Again\n");
                System.out.println("Do you want to add more music? (Y/N): ");
                addMore = scanner.nextLine();
            }
            
        } while (addMore.equalsIgnoreCase("Y"));

        //Write the musicList to the database
        writeMusicListToFile(musicDatabase, "MU2004Database.txt");
        
    }
    
    //Function to ensure that the music file does have a path and exists on the computer
    public static boolean doesFileExist(String filePath) 
    {
        File file = new File(filePath);
        return file.exists();
    }
    
    //Function that will read from the database to ensure no data is rewritten
    public static void readMusicListFromFile(String filename) {
    try (Scanner scanner = new Scanner(new File(filename))) {
        while (scanner.hasNextLine()) {
            String title = scanner.nextLine().substring("Title: ".length());
            String filePath = scanner.nextLine().substring("File Path: ".length());
            String description = scanner.nextLine().substring("Description: ".length());
            scanner.nextLine(); // Consume empty line

            musicDatabase.add(new Music(title, new File(filePath), description));
        }
    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + e.getMessage());
    }
}
    //Takes the information that was given to the program and adds the metadata to the database
    public static void writeMusicListToFile(ArrayList<Music> musicDatabase, String filename) 
    {
        try (PrintWriter writer = new PrintWriter(filename)) 
        {
            for (Music music : musicDatabase) 
            {
                writer.println("Title: " + music.getTitle());
                writer.println("File Path: " + music.getMusicFile());
                writer.println("Description: " + music.getDescription());
                addAudioFile(music.getMusicFile().toString()); 
                writer.println();
            }
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    
    
    //Function that will create a copy based off the absolute path and paste it into the audio folder
    public static void addAudioFile(String filePath) 
    {
        //ensures that audio files can be transferred to the audio folder
        try 
        {
            File sourceFile = new File(filePath);
            String fileName = sourceFile.getName();
            File destinationDir = new File("audio");
            File destinationFile = new File(destinationDir, fileName);

            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) 
            {
                fos.write(buffer, 0, length);
            }
            
            // Close streams
            fis.close();
            fos.close();
        }   
        catch (IOException e) 
        {
            System.out.println("\nError copying file: " + e.getMessage());
        }
    }
    
    //Menu for editing a music metadata and changing the music audio file
    static void editMusic() 
    {

        //Display a menu for selecting which data to edit
        System.out.println("\nSelect which item to edit:");
        System.out.println("1) Music Name");
        System.out.println("2) Music File");
        System.out.println("3) Description");
        Scanner scanner = new Scanner(System.in);
        int editChoice = scanner.nextInt();
        scanner.nextLine();

        switch (editChoice) 
        {
            case 1:
                //Edit Music Name
                editMusicName(musicDatabase);
                break;
            case 2:
                //Edit Music File
                editMusicFile(musicDatabase);
                break;
            case 3:
                //Edit Description
                editDescription(musicDatabase);
                break;
            default:
                System.out.println("Invalid choice.");
            return;
        }

        //Write the updated music arrayList back to the database
        writeMusicListToFile(musicDatabase, "MU2004Database.txt");

    }

    //Edits specifically the name of the music that was entered
    static void editMusicName(ArrayList<Music> musicDatabase) 
    {
        //If no music are in the database then the program will return to main menu
        if(musicDatabase.size() == 0)
        {
            System.out.println("No data available to edit\n");
            return;
        }

        //Display a list of music items to let the user choose which song to edit
        System.out.println("\nSelect the music you want to edit:");
        for (int i = 0; i < musicDatabase.size(); i++) 
        {
            System.out.println((i + 1) + ") " + musicDatabase.get(i).getTitle());
        }

        //Prompt the user to select a music
        System.out.println("Enter the music number: ");
        Scanner scanner = new Scanner(System.in);
        int selectedIndex = scanner.nextInt();
        scanner.nextLine();

        //Adjusting index to ensure editing is done right
        selectedIndex--;

        System.out.println();
        
        if (selectedIndex >= 0 && selectedIndex < musicDatabase.size())
        {
            //Prompts for new data
            System.out.println("Enter the new music name: ");
            String newName = scanner.nextLine();
            musicDatabase.get(selectedIndex).setTitle(newName);
            System.out.println("\nMusic name edited successfully.");
        } 
    }
    
    static void editMusicFile(ArrayList<Music> musicDatabase) 
    {
        //If no music are in the database then the program will return to main menu
        if(musicDatabase.size() == 0)
        {
            System.out.println("No data available to edit\n");
            return;
        }
        
        //Display a list of music for the user to choose
        System.out.println("\nSelect the music you want to edit:");
        for (int i = 0; i < musicDatabase.size(); i++) 
        {
            System.out.println((i + 1) + ") " + musicDatabase.get(i).getTitle());
        }

        //Prompt the user to select a file to edit
        System.out.print("Enter the music number: \n");
        Scanner scanner = new Scanner(System.in);
        int selectedIndex = scanner.nextInt();
        scanner.nextLine();

        //Adjusting index
        selectedIndex--;

        System.out.println();
        
        //File is deleted and updated with new file that user enters
        if (selectedIndex >= 0 && selectedIndex < musicDatabase.size()) 
        {
            deleteAudioFile(musicDatabase.get(selectedIndex).getMusicFile().toString());
            System.out.println("Enter the new music file path: ");
            String newFilePath = scanner.nextLine();
            while (!doesFileExist(newFilePath)) 
            {
                System.out.println("Invalid file path. Please enter a valid file path.\n");
                System.out.println("Enter the new file path: ");
                newFilePath = scanner.nextLine();
            }
            musicDatabase.get(selectedIndex).setMusicFile(new File(newFilePath));
            addAudioFile(musicDatabase.get(selectedIndex).getMusicFile().toString());
            System.out.println("\nMusic file edited successfully.");
        }   
    }

    static void editDescription(ArrayList<Music> musicDatabase) 
    {
        //If no music are in the database then the program will return to main menu
        if(musicDatabase.size() == 0)
        {
            System.out.println("No data available to edit\n");
            return;
        }
        
        //Display a list of music for the user to select from
        System.out.println("\nSelect the music you want to edit:");
        for (int i = 0; i < musicDatabase.size(); i++) 
        {
            System.out.println((i + 1) + ") " + musicDatabase.get(i).getTitle());
        }

        //User is prompt to select song listed
        System.out.print("Enter the music number: \n");
        Scanner scanner = new Scanner(System.in);
        int selectedIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        //Adjusting
        selectedIndex--;

        System.out.println();
        
        //Description gets updated with new info
        if (selectedIndex >= 0 && selectedIndex < musicDatabase.size()) 
        {
            // Selected a valid music item
            System.out.println("Enter the new description: ");
            String newDescription = scanner.nextLine();
            musicDatabase.get(selectedIndex).setDescription(newDescription);
            System.out.println("\nMusic description edited successfully.");
        } 
    }
    
    static void deleteMusic() 
    {
        //If no music are in the database then the program will return to main menu
        if(musicDatabase.size() == 0)
        {
            System.out.println("No data available to delete\n");
            return;
        }
        
        //Display a list of music for the user to select
        System.out.println("\nSelect the music item you want to delete:");
        for (int i = 0; i < musicDatabase.size(); i++) 
        {
            System.out.println((i + 1) + ") " + musicDatabase.get(i).getTitle());
        }

        //Prompt the user to select a song to delete
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the number of the music item to delete: ");
        int selectedIndex = scanner.nextInt();
        scanner.nextLine();

        //Adjustment
        selectedIndex--;
        
        //Confirms to user if they want to delete audio and data from audio folder
        if (selectedIndex >= 0 && selectedIndex < musicDatabase.size()) 
        {
            Music selectedMusic = musicDatabase.get(selectedIndex);

            //Confirm deletion
            System.out.println("\nAre you sure you want to delete the following music? (Y/N)\n----------------------------------------");
            System.out.println("Title: " + selectedMusic.getTitle());
            System.out.println("File Path: " + selectedMusic.getMusicFile());
            System.out.println("Description: " + selectedMusic.getDescription());
            String confirm = scanner.nextLine();

            //If confirmed the file and audio will be deleted
            if (confirm.equalsIgnoreCase("Y")) 
            {
                //Delete the music from the database
                musicDatabase.remove(selectedIndex);
                //Write the updated music list back to the database
                writeMusicListToFile(musicDatabase, "MU2004Database.txt");
                //Delete the audio file from the audio folder
                System.out.println();
                deleteAudioFile(selectedMusic.getMusicFile().toString());
                System.out.println("Music item deleted successfully.");
            } 
            else 
            {
                System.out.println("Deletion cancelled.");
            }
        } 
    }
    
    //Deletion of audio file
    public static void deleteAudioFile(String filePath) 
    {
        File fileToDelete = new File("audio", new File(filePath).getName());
        fileToDelete.delete();
    }
    
    //Program execution
    public static void main(String[] args) 
    {
        readMusicListFromFile("MU2004Database.txt");
        mainMenu();
    }    
}
