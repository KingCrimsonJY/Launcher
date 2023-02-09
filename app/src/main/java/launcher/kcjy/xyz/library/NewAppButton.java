package launcher.kcjy.xyz.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NewAppButton extends LinearLayout {
	public NewAppButton(Context mContext,int img,String appname,float textsize){
        super(mContext);
        setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1));
        setOrientation(LinearLayout.VERTICAL);
        setWeightSum(3);
        setClickable(true);
        
        LinearLayout imagelayout = new LinearLayout(mContext);
        imagelayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,2));
        imagelayout.setGravity(Gravity.CENTER);
        
        ImageView image = new ImageView(mContext);
        image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        image.setImageResource(img);
        image.setPadding(0,14,0,10);
        
        LinearLayout namelayout = new LinearLayout(mContext);
        namelayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1));
        namelayout.setGravity(Gravity.CENTER|Gravity.TOP);
        
        TextView name = new TextView(mContext);
        name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        name.setTextSize(textsize);
        name.setTextColor(0xffffffff);
        name.setText(appname);
        
        imagelayout.addView(image);
        namelayout.addView(name);
        addView(imagelayout);
        addView(namelayout);
    }
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

    }
	
}