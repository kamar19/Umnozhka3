package com.firstSet.umnozhka3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firstSet.umnozhka3.R
import com.firstSet.umnozhka3.databinding.FragmentFirstBinding
import java.io.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Пауза между командами
     *
     * @param i секунд
     * @return команда
     */
    fun doSleep(i: Int): String? {
        return "adb shell 'sleep $i' ;"
    }

    /**
     * Жест свайп
     *
     * @param x1 откуда
     * @param y1 откуда
     * @param x2 куда
     * @param y2 куда
     * @return команда
     */
    fun doSwipe(x1: Int, y1: Int, x2: Int, y2: Int): String? {
        return "adb shell input swipe $x1 $y1 $x2 $y2 ;"
    }

    /**
     * Жест тап
     *
     * @param x
     * @param y
     * @return команда
     */
    fun doTap(x: Int, y: Int): String? {
        return "adb shell input tap $x $y ;"
    }

    /**
     * Ввод текста в поле ввода
     *
     * @param text текст
     * @return команда
     */
    fun doInputText(text: String): String? {
        return "adb shell input text $text ;"
    }

    /**
     * Нажатие кнопки
     *
     * @param keycode код кнопки
     * @return команда
     */
    fun doInputKeyevent(keycode: Int): String? {
        return "adb shell input keyevent $keycode ;"
    }

    /**
     * Вывод сообщения (которое потом во входном потоке ловится)
     *
     * @param message сообщение
     * @return команда
     */
    fun echo(message: String): String? {
        return "echo '$message' ;"
    }

    fun runCommand(command: String?) {

        // Чтобы не вис интерфейс, запускаем в другом потоке
        Thread {
            var out: OutputStream? = null
            var `in`: InputStream? = null
            try { // Отправляем скрипт в рантайм процесс
//                val child = Runtime.getRuntime().exec(command) // Выходной и входной потоки
                val child = Runtime.getRuntime().exec(arrayOf("su", "-c", "system/bin/sh"))
                val stdin = DataOutputStream(child.outputStream) //Скрипт
                //Скрипт
                stdin.writeBytes(command)







                out = child.outputStream
                `in` = child.inputStream

                //Входной поток может что-нибудь вернуть
                val bufferedReader = BufferedReader(InputStreamReader(`in`))
                var line: String?
                var result: String? = ""
                while (bufferedReader.readLine().also { line = it } != null) result += line

                //Обработка того, что он вернул
                handleBashCommandsResult(result)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (`in` != null) {
                    try {
                        `in`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (out != null) {
                    try {
                        out.flush()
                        out.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    private fun handleBashCommandsResult(result: String?) {

        if (result != null) {
            if (result.contains("SCRIPT_FINISHED")) { //Здесь делаем всё что хотели сделать после завершение скрипта
            } else { //А вот здесь можно сделать что-нибудь после другого скрипта
            } //            else {
            //            //А вот здесь можно сделать всё остальное
            //        }
        }
    }


}