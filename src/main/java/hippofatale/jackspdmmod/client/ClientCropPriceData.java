package hippofatale.jackspdmmod.client;

public class ClientCropPriceData {
    private static int[] cropPriceList = {512, 1300, 960, 640, 384, 384};

    public static int getCropPrice(int cropIndex) {
        return cropPriceList[cropIndex];
    }

    public static void setCropPriceList(int[] cropPriceList) {
        ClientCropPriceData.cropPriceList = cropPriceList.clone();
    }
}
