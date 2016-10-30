package com.appspace.evyalerts.util;

import com.appspace.evyalerts.model.ProvinceCentroid;

/**
 * Created by siwaweswongcharoen on 9/14/2016 AD.
 */
public class DistanceUtil {

    private static DistanceUtil instance;

    public static DistanceUtil getInstance() {
        if (instance == null)
            instance = new DistanceUtil();
        return instance;
    }

    private DistanceUtil() {

    }

    public ProvinceCentroid[] provinceCentroids = new ProvinceCentroid[]{
            new ProvinceCentroid("AMNAT CHAROEN", 15.89210136, 104.740038),
            new ProvinceCentroid("ANG THONG", 14.62291196, 100.3491015),
            new ProvinceCentroid("BANGKOK", 13.77199178, 100.6236236),
            new ProvinceCentroid("BUENGKAN", 18.14866515, 103.7090257),
            new ProvinceCentroid("BURI RAM", 14.8205145, 102.9560383),
            new ProvinceCentroid("CHACHOENGSAO", 13.60754547, 101.429381),
            new ProvinceCentroid("CHAI NAT", 15.13274388, 100.0249519),
            new ProvinceCentroid("CHAIYAPHUM", 16.03141959, 101.8177117),
            new ProvinceCentroid("CHANTHABURI", 12.87779172, 102.1288566),
            new ProvinceCentroid("CHIANG MAI", 18.79134851, 98.72550135),
            new ProvinceCentroid("CHIANG RAI", 19.84678331, 99.86587225),
            new ProvinceCentroid("CHON BURI", 13.19162188, 101.2004709),
            new ProvinceCentroid("CHUMPHON", 10.34549052, 99.06237964),
            new ProvinceCentroid("KALASIN", 16.62803225, 103.6224728),
            new ProvinceCentroid("KAMPHAENG PHET", 16.3318005, 99.53241359),
            new ProvinceCentroid("KANCHANABURI", 14.58309144, 99.04966686),
            new ProvinceCentroid("KHON KAEN", 16.408402, 102.5786254),
            new ProvinceCentroid("KRABI", 8.144358622, 99.02021713),
            new ProvinceCentroid("LAMPANG", 18.32504919, 99.51110147),
            new ProvinceCentroid("LAMPHUN", 18.11853866, 98.95464001),
            new ProvinceCentroid("LOEI", 17.40661747, 101.6319998),
            new ProvinceCentroid("LOP BURI", 15.09466901, 100.8994249),
            new ProvinceCentroid("MAE HONG SON", 18.80974096, 98.02940353),
            new ProvinceCentroid("MAHA SARAKHAM", 15.99905635, 103.1660914),
            new ProvinceCentroid("MUKDAHAN", 16.56663338, 104.5157384),
            new ProvinceCentroid("NAKHON NAYOK", 14.21702847, 101.1709717),
            new ProvinceCentroid("NAKHON PATHOM", 13.92598547, 100.1055152),
            new ProvinceCentroid("NAKHON PHANOM", 17.39019693, 104.4295171),
            new ProvinceCentroid("NAKHON RATCHASIMA", 14.958661, 102.1076),
            new ProvinceCentroid("NAKHON SAWAN", 15.68315146, 100.153117),
            new ProvinceCentroid("NAKHON SI THAMMARAT", 8.378763413, 99.78654659),
            new ProvinceCentroid("NAN", 18.85188804, 100.8325982),
            new ProvinceCentroid("NARATHIWAT", 6.177211733, 101.7190387),
            new ProvinceCentroid("NONG BUA LAMPHU", 17.18204381, 102.3018143),
            new ProvinceCentroid("NONG KHAI", 17.94072459, 102.8244955),
            new ProvinceCentroid("NONTHABURI", 13.92413065, 100.3933587),
            new ProvinceCentroid("PATHUM THANI", 14.06521741, 100.6818958),
            new ProvinceCentroid("PATTANI", 6.730352199, 101.3519935),
            new ProvinceCentroid("PHANGNGA", 8.673361802, 98.41789104),
            new ProvinceCentroid("PHATTHALUNG", 7.512592592, 100.065634),
            new ProvinceCentroid("PHAYAO", 19.23341129, 100.1875159),
            new ProvinceCentroid("PHETCHABUN", 16.26127496, 101.1446701),
            new ProvinceCentroid("PHETCHABURI", 12.94791166, 99.61919407),
            new ProvinceCentroid("PHICHIT", 16.25989255, 100.3412716),
            new ProvinceCentroid("PHITSANULOK", 16.98312515, 100.5427161),
            new ProvinceCentroid("PHRA NAKHON SI AYUTTHAYA", 14.34520102, 100.5276959),
            new ProvinceCentroid("PHRAE", 18.20087287, 100.065789),
            new ProvinceCentroid("PHUKET", 7.965866164, 98.34608854),
            new ProvinceCentroid("PRACHIN BURI", 14.05209605, 101.6479833),
            new ProvinceCentroid("PRACHUAP KHIRI KHAN", 11.9471555, 99.6347722),
            new ProvinceCentroid("RANONG", 9.972819164, 98.69898264),
            new ProvinceCentroid("RATCHABURI", 13.5341618, 99.57873747),
            new ProvinceCentroid("RAYONG", 12.85539553, 101.4263778),
            new ProvinceCentroid("ROI ET", 15.91965205, 103.8139264),
            new ProvinceCentroid("SA KAEO", 13.78605948, 102.3205503),
            new ProvinceCentroid("SAKON NAKHON", 17.38958767, 103.8247359),
            new ProvinceCentroid("SAMUT PRAKAN", 13.59561066, 100.7094533),
            new ProvinceCentroid("SAMUT SAKHON", 13.57041236, 100.2127709),
            new ProvinceCentroid("SAMUT SONGKHRAM", 13.39737186, 99.95345265),
            new ProvinceCentroid("SARABURI", 14.62746561, 101.0157184),
            new ProvinceCentroid("SATUN", 6.825876621, 99.92717294),
            new ProvinceCentroid("SI SA KET", 14.85745892, 104.3693968),
            new ProvinceCentroid("SING BURI", 14.91341808, 100.3460093),
            new ProvinceCentroid("SONGKHLA", 6.943342835, 100.54244),
            new ProvinceCentroid("SUKHOTHAI", 17.25985314, 99.70978321),
            new ProvinceCentroid("SUPHAN BURI", 14.60911748, 99.89252941),
            new ProvinceCentroid("SURAT THANI", 9.052006233, 99.09106492),
            new ProvinceCentroid("SURIN", 14.88581765, 103.6559988),
            new ProvinceCentroid("TAK", 16.71700281, 98.79057472),
            new ProvinceCentroid("TRANG", 7.53338694, 99.60457045),
            new ProvinceCentroid("TRAT", 12.31528071, 102.521379),
            new ProvinceCentroid("UBON RATCHATHANI", 15.1838829, 105.1112145),
            new ProvinceCentroid("UDON THANI", 17.42551773, 102.8663231),
            new ProvinceCentroid("UTHAI THANI", 15.34959565, 99.47823104),
            new ProvinceCentroid("UTTARADIT", 17.75072702, 100.5169014),
            new ProvinceCentroid("YALA", 6.191028323, 101.2288061),
            new ProvinceCentroid("YASOTHON", 15.89689193, 104.3390659)
    };

    public double distanceBetween(double lat1, double lng1, ProvinceCentroid provinceCentroid) {
        double lat2 = provinceCentroid.lat;
        double lng2 = provinceCentroid.lng;
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    public boolean isTooFar(double distance) {
        return distance > 100000;
    }
}
