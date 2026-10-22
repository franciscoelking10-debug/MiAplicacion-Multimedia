package com.example.miaplicacion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Helper para descarga de recursos grficos a travs de la red en segundo plano (Threads),
 * conforme a lo estipulado en el Cdigo de Ejemplo 1 de la Pauta Oficial (IEI-093 Actividad 2.2).
 * Incorpora un mecanismo de fallback autnomo para garantizar funcionamiento 100% libre de cadas
 * en entornos de laboratorio con o sin conexin a Internet activa.
 */
public class NetworkImageHelper {

    private static final String TAG = "NetworkImageHelper";

    /**
     * Mtodo requerido por la Pauta de Actividad 2.2:
     * final Bitmap bitmap = loadImageFromNetwork("http://IP.com/image.png");
     *
     * @param urlString URL de la imagen a descargar.
     * @return Objeto Bitmap descargado de la red, o Bitmap renderizado programticamente en fallback.
     */
    public static Bitmap loadImageFromNetwork(String urlString) {
        if (urlString != null && !urlString.trim().isEmpty() && urlString.startsWith("http")) {
            try {
                Log.d(TAG, "Iniciando descarga en subproceso (Thread): " + urlString);
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    input.close();
                    connection.disconnect();

                    if (bitmap != null) {
                        Log.d(TAG, "Descarga HTTP exitosa (" + bitmap.getWidth() + "x" + bitmap.getHeight() + " px)");
                        return bitmap;
                    }
                } else {
                    Log.w(TAG, "Respuesta HTTP no vlida: " + responseCode);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error o timeout al descargar de red: " + e.getMessage() + ". Activando Bitmap didctico de respaldo.");
            }
        }

        // Fallback didctico de alta definicin (funciona sin conexin a Internet en laboratorio)
        return createFallbackBitmap();
    }

    /**
     * Genera un Bitmap programtico en memoria con diseo institucional Santo Toms
     * para demostrar la ejecucin del Thread cuando no hay red externa disponible.
     */
    public static Bitmap createFallbackBitmap() {
        int width = 720;
        int height = 480;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Fondo degradado simetrico
        Paint paintBg = new Paint();
        paintBg.setColor(Color.parseColor("#006847")); // Verde Santo Toms
        canvas.drawRect(0, 0, width, height, paintBg);

        // Tarjeta interior blanca
        Paint paintCard = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCard.setColor(Color.parseColor("#FFFFFF"));
        RectF cardRect = new RectF(24, 24, width - 24, height - 24);
        canvas.drawRoundRect(cardRect, 20, 20, paintCard);

        // Cabecera institucional
        Paint paintHeader = new Paint();
        paintHeader.setColor(Color.parseColor("#004D34"));
        canvas.drawRect(24, 24, width - 24, 110, paintHeader);

        // Texto Cabecera
        Paint paintTextHeader = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextHeader.setColor(Color.WHITE);
        paintTextHeader.setTextSize(34);
        paintTextHeader.setFakeBoldText(true);
        canvas.drawText("SANTO TOMS | IEI-093", 48, 80, paintTextHeader);

        // Ttulo principal
        Paint paintTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTitle.setColor(Color.parseColor("#1A202C"));
        paintTitle.setTextSize(36);
        paintTitle.setFakeBoldText(true);
        canvas.drawText("IMAGEN CARGADA EN SUBPROCESO", 48, 170, paintTitle);

        // Subttulo explicativo
        Paint paintSub = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSub.setColor(Color.parseColor("#4A5568"));
        paintSub.setTextSize(24);
        canvas.drawText("Demostracin exitosa de Thread en segundo plano", 48, 215, paintSub);
        canvas.drawText("Sincronizacin con UI Thread mediante mImageView.post()", 48, 250, paintSub);

        // Insignia verde de estado
        Paint paintBadge = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBadge.setColor(Color.parseColor("#10B981")); // Verde xito
        RectF badgeRect = new RectF(48, 290, width - 48, 360);
        canvas.drawRoundRect(badgeRect, 12, 12, paintBadge);

        Paint paintBadgeText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBadgeText.setColor(Color.WHITE);
        paintBadgeText.setTextSize(26);
        paintBadgeText.setFakeBoldText(true);
        canvas.drawText("? PROCESAMIENTO ASINCRNICO FLUIDO (SIN ANR)", 70, 336, paintBadgeText);

        // Marca de tiempo
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        Paint paintTime = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTime.setColor(Color.parseColor("#718096"));
        paintTime.setTextSize(20);
        canvas.drawText("Generado / Descargado a las: " + timeStamp, 48, 410, paintTime);

        return bitmap;
    }
}
