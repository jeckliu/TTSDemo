package com.jeck.ttsdemo;

import android.content.Context;
import android.os.Environment;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/***
 * Created by Jeck.Liu on 2017/7/17 0017.
 */

public class TtsManager {
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_ch_speech_female.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_ch_text.dat";
    private static String mSampleDirPath;
    private static SpeechSynthesizer mSpeechSynthesizer;

    public static void init(Context context){
        File file = new File(Environment.getExternalStorageDirectory(),context.getPackageName());
        file.mkdirs();
        mSampleDirPath = file.getAbsolutePath();

        copyFromAssetsToSdcard(context,false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + File.separator + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(context,false, TEXT_MODEL_NAME, mSampleDirPath + File.separator + TEXT_MODEL_NAME);
        startTTS(context);
    }

    public static void speech(){
        mSpeechSynthesizer.speak("测试");
    }

    // 初始化语音合成客户端并启动
    private static void startTTS(Context context) {
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(context);
        // 设置语音合成状态监听器
//        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
        mSpeechSynthesizer.setApiKey("hmURAUMwZgXCPqFnkhFR9qbj", "5a031f290aeeeaaf41c695b36466985a");
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId("9585862");
        // 设置语音合成文本模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath+File.separator+TEXT_MODEL_NAME);
        // 设置语音合成声音模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath+File.separator+SPEECH_FEMALE_MODEL_NAME);
        // 设置语音合成声音授权文件
//        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, "your_licence_path");
        // 获取语音合成授权信息
//        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        // 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
//        if (authInfo.isSuccess()) {
        mSpeechSynthesizer.initTts(TtsMode.MIX);
//        } else {
        // 授权失败
//        }
    }

    /**
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private static void copyFromAssetsToSdcard(Context context, boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = context.getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
