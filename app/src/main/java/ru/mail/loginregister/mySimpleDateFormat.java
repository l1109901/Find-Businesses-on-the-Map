package ru.mail.loginregister;

/**
 * Created by gafur on 6/3/2016.
 */
public class mySimpleDateFormat {

    private String hh_mm_aa;

    public mySimpleDateFormat(String hh_mm_aa){
        this.hh_mm_aa=hh_mm_aa;
    }

    public int get_hour(){
        int hour=0;
        String[] sp=hh_mm_aa.split(" : ");
        try {
            hour = Integer.parseInt(sp[0]);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse" + nfe);
        }
        return hour;
    }

    public int get_minute(){
        int minute=0;
        String[] sp=hh_mm_aa.split(" : ");
        String[] sp2=sp[1].split(" ");

        try {
            minute = Integer.parseInt(sp2[0]);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse" + nfe);
        }

        return minute;
    }

    public int get_ekle_cikar(){
        int ekle_cikar=0;

        String[] sp=hh_mm_aa.split(" : ");
        String[] sp2=sp[1].split(" ");

        if(sp2[1].equals("PM")){
            ekle_cikar=12;
        }

        return ekle_cikar;
    }
}
