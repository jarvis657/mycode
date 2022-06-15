package origin.base.files;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/8
 * @Desc:
 **/
public class FileRunner {

    public static void main(String[] args) throws Exception {
        FileMonitor fileMonitor = new FileMonitor(1000);
        fileMonitor.monitor("/Users/zzs/temp/", new FileListener());
        fileMonitor.start();
    }
}

