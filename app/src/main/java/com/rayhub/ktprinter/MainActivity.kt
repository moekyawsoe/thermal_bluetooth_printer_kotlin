package com.rayhub.ktprinter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import com.rayhub.ktprinter.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

class MainActivity : AppCompatActivity(), PrintingCallback {

    private var printing: Printing? = null;
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        initPrinting()
        binding.btnPairunpair.setOnClickListener {
            if(Printooth.hasPairedPrinter()){
                Printooth.removeCurrentPrinter()
            }else {
                startActivityForResult(Intent(this@MainActivity, ScanningActivity::class.java), ScanningActivity.SCANNING_FOR_PRINTER)
                changePairAndUnpair()
            }
        }

        binding.btnPrintimage.setOnClickListener {
            if(!Printooth.hasPairedPrinter()){
                startActivityForResult(Intent(this@MainActivity, ScanningActivity::class.java), ScanningActivity.SCANNING_FOR_PRINTER)
            }else{
                printImage()
            }
        }

        binding.btnPrint.setOnClickListener {
            if(!Printooth.hasPairedPrinter()){
                startActivityForResult(Intent(this@MainActivity, ScanningActivity::class.java), ScanningActivity.SCANNING_FOR_PRINTER)
            }else{
                printText()
            }
        }
    }

    private fun printText() {
        val printTables = ArrayList<Printable>()
        printTables.add(RawPrintable.Builder(byteArrayOf(27, 100, 4)).build())

        printTables.add(TextPrintable.Builder()
            .setText("Mega Network Company Limited")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON)
            .setNewLinesAfter(5)
            .build())

        printTables.add(TextPrintable.Builder()
            .setText("No.100, Main Road")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setNewLinesAfter(2)
            .build())

        printTables.add(TextPrintable.Builder()
            .setText("Sittwe")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setNewLinesAfter(2)
            .build())

        printTables.add(TextPrintable.Builder()
            .setText("09123456789")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setNewLinesAfter(2)
            .build())

        printTables.add(TextPrintable.Builder()
            .setText("09123456789")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_LEFT)
            .setNewLinesAfter(2)
            .build())

        printTables.add(TextPrintable.Builder()
            .setText("Thanks for using us our FTTH Service")
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setNewLinesAfter(5)
            .build())

        printing!!.print(printTables)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun printImage() {
        val printTables = ArrayList<Printable>()

        var drawable: Drawable = applicationContext.resources.getDrawable(R.drawable.download)
        var bitmap: Bitmap = (drawable as BitmapDrawable).bitmap

        printTables.add(ImagePrintable.Builder(bitmap).build())
        printing!!.print(printTables)
//
//        Picasso.get().load("https://www.clipartmax.com/png/middle/61-616333_android-black-logo-hd-wallpapers-android-black-logo-android-logo-black-png.png")
//            .into(object: Target{
//                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//
//                }
//
//                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//
//                }
//
//                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//
//                }
//
//            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK)
            initPrinting()
        changePairAndUnpair()
    }

    private fun initPrinting() {
        if(Printooth.hasPairedPrinter()){
            printing = Printooth.printer()
        }
        if(printing != null){
            printing!!.printingCallback = this
        }
    }

    @SuppressLint("SetTextI18n")
    private fun changePairAndUnpair() {
        if(Printooth.hasPairedPrinter()){
            binding.btnPairunpair.text = "Unpair ${Printooth.getPairedPrinter()!!.name}"
        }else {
            binding.btnPairunpair.text = "Paired with ${Printooth.getPairedPrinter()!!.name}"
        }
    }

    override fun connectingWithPrinter() {
        Toast.makeText(this, "Connecting to printer", Toast.LENGTH_LONG).show()
    }

    override fun connectionFailed(error: String) {
        Toast.makeText(this, "Failed: $error", Toast.LENGTH_LONG).show()
    }

    override fun onError(error: String) {
        Toast.makeText(this, "Failed: $error", Toast.LENGTH_LONG).show()
    }

    override fun onMessage(message: String) {
        Toast.makeText(this, "Failed: $message", Toast.LENGTH_LONG).show()
    }

    override fun printingOrderSentSuccessfully() {
        Toast.makeText(this, "Order sent to printer", Toast.LENGTH_LONG).show()
    }
}