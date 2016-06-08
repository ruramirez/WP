package com.vikinsoft.wp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class ProductoDetalle extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GlobalClass appstate = (GlobalClass) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null)
        {
            value = b.getInt("id_producto");
            producto = appstate.getProductobyID(value);
        }

        HashMap<String,String> url_maps = new HashMap<String, String>();
        for (FotosProductos fotos : producto.getFotos())
        {
            url_maps.put(String.valueOf(fotos.getId()),fotos.getUrl());
        }

        for(String name : url_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //.description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopAutoCycle();

        TextView precio = (TextView) findViewById(R.id.precioymoneda_detalle);
        precio.setText(producto.getMoneda().getSimbolo()+" "+producto.getPrecio());
        TextView titulo = (TextView) findViewById(R.id.titulo_detalle);
        titulo.setText(producto.getNombre());
        TextView descr = (TextView) findViewById(R.id.descripcion_detalle);
        descr.setText(producto.getDescripcion());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
