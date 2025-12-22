package com.projects.moneycompose.view.exportReport

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.moneycompose.core.navigation.Screens
import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.use_case.MoneyUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExportReportViewModel.Factory::class)
class ExportReportViewModel @AssistedInject constructor(
    @Assisted val navKey: Screens,
    private val moneyUseCases: MoneyUseCases
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Screens): ExportReportViewModel
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun generatePDF(context: Context, month: String, year: String) {
        viewModelScope.launch {
            val report = moneyUseCases.getReportPDF(month = month, year = year)
            report?.let {
                generateReportPDF(context = context, it)
            } ?: Toast.makeText(context, "No hay datos para el reporte", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun generateReportPDF(context: Context, exportEntity: ExportEntity) {
        val pdfDocument = PdfDocument()

        val pageWidth = 350
        val pageHeight = 600
        var pageNumber = 1

        fun newPage(): Pair<PdfDocument.Page, Canvas> {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            val page = pdfDocument.startPage(pageInfo)
            return page to page.canvas
        }

        var (page, canvas) = newPage()
        val paint = Paint()
        var y = 40

        paint.color = Color.BLACK
        paint.textSize = 18f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Reporte de Gastos", 80f, y.toFloat(), paint)

        paint.textSize = 14f
        paint.typeface = Typeface.DEFAULT_BOLD
        y += 30
        canvas.drawText("Mes: ${exportEntity.monthReport}", 40f, y.toFloat(), paint)

        y += 20
        canvas.drawText("Año: ${exportEntity.yearReport}", 40f, y.toFloat(), paint)

        y += 25
        canvas.drawText("Gastos:", 40f, y.toFloat(), paint)

        paint.textSize = 12f
        paint.typeface = Typeface.DEFAULT
        var index = 1
        exportEntity.listSpent.forEach { item ->
            y += 20

            if (y > pageHeight - 40) {
                pdfDocument.finishPage(page)
                pageNumber++
                val (newPg, newCanvas) = newPage()
                page = newPg
                canvas = newCanvas
                y = 40
            }

            canvas.drawText(
                "$index. ${item.description}:   S/.${item.money}   (${item.data})",
                40f,
                y.toFloat(),
                paint
            )
            index++
        }

        pdfDocument.finishPage(page)

        val fileName =
            "REPORTE DE GASTOS: ${exportEntity.monthReport}-${exportEntity.yearReport}.pdf"
        savePdfToDownloads(context, fileName, pdfDocument)

        pdfDocument.close()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun savePdfToDownloads(context: Context, fileName: String, pdfDocument: PdfDocument) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
                Toast.makeText(context, "PDF guardado en Descargas", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(context, "Error al crear archivo", Toast.LENGTH_SHORT).show()
    }

}
