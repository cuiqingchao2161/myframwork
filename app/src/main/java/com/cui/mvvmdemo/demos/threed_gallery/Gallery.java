package com.cui.mvvmdemo.demos.threed_gallery;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cui.mvvmdemo.R;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author lixx
 * @brief
 * @createTime 2016/11/3
 */

public class Gallery extends Activity {
    private ViewPager gallery;
    private GalleryAdapter mAdapter;
    private int curPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery);

        gallery = (ViewPager) findViewById(R.id.gallery);
        gallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
//                View view = gallery.getChildAt(position);
//                TextView name = (TextView) view.findViewById(R.id.name);
//                if (curPos != position) {
//                    name.setVisibility(View.GONE);
//                } else {
//                    name.setVisibility(View.VISIBLE);
//                }

            }

            @Override
            public void onPageSelected(int position) {
//                curPos = position;
//                View view = gallery.getChildAt(position);
//                TextView name = (TextView) view.findViewById(R.id.name);
//                name.setVisibility(View.VISIBLE);
//                mAdapter.setSelectPos(position);
                updateView(position);
                Toast.makeText(Gallery.this, position + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        gallery.setOffscreenPageLimit(5);
        gallery.setPageTransformer(true, new MyTransformation());
        //gallery.setPageTransformer(true, new ZoomOutPageTransformer());

        mAdapter = new GalleryAdapter();
        gallery.setAdapter(mAdapter);
        //mAdapter.setData();

        findViewById(R.id.activity_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gallery.dispatchTouchEvent(motionEvent);
            }
        });
    }

    private void updateView(int pos) {
        for (int i = 0; i < gallery.getChildCount(); i++) {
            View view = gallery.getChildAt(pos);
            TextView name = (TextView) view.findViewById(R.id.name);
            if (i == gallery.getCurrentItem()) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }
    }

    class GalleryAdapter extends PagerAdapter {
        private int selectPos = 0;
        private List<View> viewList = new ArrayList<View>();
        Integer[] images = {R.mipmap.img0001, R.mipmap.img0030,
                R.mipmap.img0100, R.mipmap.img0130,
                R.mipmap.img0230, R.mipmap.img0330, R.mipmap.img0354};
        String[] names = {"电影", "冰封重生之门", "超人", "GOLD WAR", "别有动机", "变脸2", "韩国电影"};

        public GalleryAdapter() {

//            for (int i = 0; i < images.length; i++) {
//                View view = LayoutInflater.from(Gallery.this).inflate(R.layout
//                        .gallery_item, null);
//                ImageView poster = (ImageView) view.findViewById(R.id.poster);
//                poster.setImageResource(images[i]);
//                // view.setImageBitmap(ImageUtil.getReverseBitmapById(images[i],Gallery.this));
//                TextView name = (TextView) view.findViewById(R.id.name);
//                name.setText(names[i]);
//                viewList.add(view);
//            }
            // mAdapter.notifyDataSetChanged();
        }

//        private void setSelectPos(int pos) {
//            this.selectPos = pos;
//        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(Gallery.this).inflate(R.layout
                    .gallery_item, null);
            ImageView poster = (ImageView) view.findViewById(R.id.poster);
            poster.setImageResource(images[position]);
            // view.setImageBitmap(ImageUtil.getReverseBitmapById(images[i],Gallery.this));
            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(names[position]);
            //name.setText("电影");
            //viewList.add(view);
//            if (selectPos == position) {
//                name.setVisibility(View.VISIBLE);
//            } else {
//                name.setVisibility(View.GONE);
//            }
            gallery.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            gallery.removeView((View) object);
        }
    }
}
