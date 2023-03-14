/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

/**
 *
 * @author Philip
 */
public enum OccupationRural {
//    DAYLABOUR("Day Labour", "দিনমজুর", 2),
//    AGRICULTURELABOUR("Agriculture Labour", "কৃষিমজুর", 2),
//    RICKSHAWPULLER("Rickshaw Puller", "রিকশাওয়ালা", 2),
//    TRICYCLEVANPULLER("Tri Cycle Van Puller", "ভ্যানচালক", 2),
//    AUTORICKSAWPULLER("Auto Ricksaw Pulller", "অটো রিক্সা চালক",1 ),
//    COBLER("cobler", "মুচি", 2),
//    SCULPTOR("sculptor", "ভাস্কর", 2),
//    CRAFTSMAN("craftsman", "কারিগর", 2),
//    BARBER("barber", "নাপিত", 2),
//    BLACKSMITH("blacksmith", "কামার", 2),
//    PORTER("porter", "কুলি", 2),    
//    FISHERMAN("fisherman", "জেলে", 2),
//    UNEMPLOYED("Unemployed", "বেকার", 3),
//    NA("N/A", "প্রযোজ্য নয়",3),
//    OTHERS("Others", "অন্যান্য", 0);

    // new set given by wfp on 10/02/20

    /**
     *
     */
    DAYLABOUR("Day laborer", "দিন মজুর", 1),
    AGRICULTURELABOUR("Agricultural laborer", "কৃষি শ্রমিক", 1),
    RICKSHAWPULLER("Rickshaw puller", "রিকশা চালনা", 1),
    TRICYCLEVANPULLER("Van driver", "ভ্যান চালনা", 1),
    BARBER("Barber", "নরসুন্দর ", 1),
    BLACKSMITH("Blacksmith", "কামার", 1),
    POTTER("Potter", "কুমার", 1),
    WASHERMAN("Washer man", "ধোপা ", 1),
    PORTER("Porter", "কুলি", 1),
    FISHERMAN("Fisher man", "জেলে", 1),
    UNEMPLOYED("Unemployed", "বেকার", 0),
    NA("N/A", "প্রযোজ্য নয়", 0),

    /**
     *
     */
    OTHERS("Others", "অন্যান্য", 0);

    private final String displayName;
    private final String displayNameBn;
    private final int score;

    private OccupationRural(String displayName, String displayNameBn, int score) {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
        this.score = score;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayNameBn() {
        return displayNameBn;
    }

    /**
     *
     * @return
     */
    public int getScore() {
        return score;
    }

}
