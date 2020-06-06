package Spark_Stream_window;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class LogGenerator {
    public static final String FILE_PATH="./logs/";//文件指定存放的路径
    public static void main(String[] args) throws Exception {
        FileOutputStream outFile=null;
        DateFormat df= new SimpleDateFormat("yyyyMMddHHmmss");
        Random r=new Random();
        String grades[]={"[info]","[warn]","[error]","[debug]"};
        String position[]={"main","calculate","saveFile","openFile"};
        for (int i=0;i<5;i++){
            System.out.println(i);
            String filename=df.format(new Date())+".log";
            File file=createFile(FILE_PATH,filename);
            //文件已经创建了 就可以开始写日志到项目里面了
            outFile=new FileOutputStream(file);//创建一个字符输出流
            for(int j=0;j<10;j++){
                //日志格式 [级别]\t位置\tDate:时间
                String log=grades[r.nextInt(grades.length)] +"\t"
                        +position[r.nextInt(position.length)]+"\tDate"+df.format(new Date())+"\n";
                outFile.write(log.getBytes());
            }
            outFile.flush();
            outFile.close();
            Thread.sleep(r.nextInt(2000)+1000);

        }
    }
    public static File createFile(String filePath,String fileName){
        File folder=new File(filePath);
        //看这个文件夹路径不存在
        if (!folder.exists() && !folder.isDirectory()){
            System.out.println("文件夹路径不存在,创建文件:"+filePath);
            folder.mkdirs();//创建路径
        }
        //看文件存不存在
        File file=new File(filePath+fileName);
        //看这个文件是不是存在 如果存在就不创建 如果不存在 就要创建文件
        if (!file.exists()){
            System.out.println("文件不存在,创建文件:"+filePath+fileName);
            try{
                file.createNewFile();//创建一个新文件
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return file;
    }

}
