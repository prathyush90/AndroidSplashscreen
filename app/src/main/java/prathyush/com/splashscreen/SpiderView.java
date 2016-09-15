    package prathyush.com.splashscreen;

    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Color;
    import android.graphics.Paint;
    import android.os.Handler;
    import android.util.AttributeSet;
    import android.util.DisplayMetrics;
    import android.util.TypedValue;
    import android.view.View;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Random;


    /**
     * Created by prathyush on 14/09/16.
     */
    public class SpiderView extends View {

        private int NUM_OF_CIRCLES           = 30;

        private Point presentViewPoints[]    = new Point[NUM_OF_CIRCLES];
        private ArrayList<Line>   linesList  = new ArrayList<>();
        private Handler mHandler;
        Runnable mStatusChecker = null;





        private String color1 = "#EEEEEE";
        private String color2 = "#E0E0E0";
        private String color3 = "#BDBDBD";
        private String color4 = "#9E9E9E";
        private String color5 = "#757575";
        private Context mContext;
        private int mHeight;
        private int mWidth;
        Paint mCirclePaint = new Paint();
        private String circleColor = "#F5F5F5";
        long startTime;
        private boolean initFlag = false;



        public SpiderView(Context context) {
            super(context);
            this.mContext = context;


        }



        public SpiderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.mContext = context;



        }

        public SpiderView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.mContext = context;


        }

        private void init() {


startTime = System.currentTimeMillis();
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(Color.parseColor(circleColor));

                for(int i=0;i<NUM_OF_CIRCLES;i++)
                {
                    Point p = new Point();
                    Random rn = new Random();
                    int quadrant = rn.nextInt(2) + 0;
                    //p.angle  = quadrant == 0? Math.PI/6+rn.nextDouble()*Math.PI/6: (2*Math.PI/3) + rn.nextDouble()* Math.PI/6;
//                    p.angle  = quadrant == 0? rn.nextDouble()*Math.PI/2.5: (3*Math.PI/5) + rn.nextDouble()* Math.PI/2.5;
//                    p.x = p.previousX = quadrant == 0 ? rn.nextInt(mWidth/3 - 0 + 1) + 0:rn.nextInt(mWidth/3 + 1)+(2*mWidth/3);
//                    p.y = p.previousY = rn.nextInt(mHeight - (mHeight/8) + 1) + mHeight/8;

                    p.angle    = rn.nextDouble()*Math.PI;
                    p.x = p.previousX =  rn.nextInt(mWidth - 0 + 1)+0;
                    p.y = p.previousY = rn.nextInt(mHeight - 0 + 1) + 0;

                    p.radius =  dipToPixels(rn.nextInt(4) + 3);
                    p.proxdistance =  mHeight/(3*p.radius);

                    presentViewPoints[i] = p;
                }

            addHandler();


        }

        private void addHandler() {
            mHandler = new Handler();

            mStatusChecker =        new Runnable() {
               @Override
               public void run() {

                   calculatePointPosition();
                   addLines();

                   invalidate();
                   mHandler.postDelayed(mStatusChecker,10);

               }

            };
            mHandler.postDelayed(mStatusChecker,10);



        }

        private void addLines() {

            linesList.clear();

            for(int i = 0;i<NUM_OF_CIRCLES;i++)
            {
                for(int j=i+1;j<NUM_OF_CIRCLES;j++)
                {



                    Point p1 = presentViewPoints[i];
                    Point p2 = presentViewPoints[j];

                    double distance = Math.sqrt(Math.pow(Math.abs(p1.y-p2.y),2)+Math.pow(Math.abs(p1.x-p2.x),2));

                    if((distance>=p1.proxdistance || distance >= p2.proxdistance) && distance <= ((mHeight)/6))
                    {
                        Line l = new Line();
                        l.firstX  = p1.x;
                        l.firstY  = p1.y;
                        l.secondX = p2.x;
                        l.secondY = p2.y;
                         if(distance <= ((mHeight)/18))
                        {
                            l.color = color1;
                        }else if(distance <= ((mHeight)/12))
                        {
                            l.color = color2;
                        }else if(distance <= ((mHeight)/9))
                        {
                            l.color = color3;
                        }else if(distance <= ((5*mHeight)/9))
                        {
                            l.color = color4;
                        }else if(distance <= ((mHeight)/6))
                        {
                            l.color = color5;
                        }
                        linesList.add(l);
                    }



                }
            }
        }

        private void calculatePointPosition() {

            for(int i=0;i<NUM_OF_CIRCLES;i++)
            {
                Point p = presentViewPoints[i];

                double  distance = dipToPixels(1);


                p.previousX     = p.x;
                p.previousY     = p.y;
                //double degrees         = Math.toDegrees(p.angle);

//                if(p.updated % p.changeAngle == 0)
//                {
//                    p.angle = new Random().nextDouble()*Math.PI;
//                }


                p.x = p.previousX +(int) (distance * Math.cos(p.angle));
                p.y = p.previousY -(int) (distance * Math.sin(p.angle));

                if(p.x < 0 || p.x >mWidth || p.y < 0 || p.y>mHeight)
                {
                    Random rn = new Random();

                   // p.angle  = quadrant == 0? Math.PI/6+rn.nextDouble()*Math.PI/6: (2*Math.PI/3) + rn.nextDouble()* Math.PI/6;
                   // p.angle  = quadrant == 0? rn.nextDouble()*Math.PI/2.5: (3*Math.PI/5) + rn.nextDouble()* Math.PI/2.5;
                    p.angle    = rn.nextDouble()*Math.PI;
                    p.x = p.previousX =  rn.nextInt(mWidth - 0 + 1)+0;
                    p.y = p.previousY = rn.nextInt(mHeight - 0 + 1) + 0;

                    p.radius =  dipToPixels(rn.nextInt(4) + 3);
                    p.proxdistance =  mHeight/(3*p.radius);


                }
                presentViewPoints[i] = p;

            }

        }

        public float dipToPixels(float dipValue) {
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for(int i=0;i<presentViewPoints.length;i++)
            {
                Point p = presentViewPoints[i];
               canvas.drawCircle(p.x,p.y,p.radius,mCirclePaint);
            }
            for(int i=0;i<linesList.size();i++)
            {
                Paint paint = new Paint();
                Line l = linesList.get(i);

                //paint.setStrokeWidth(dipToPixels(2));
                paint.setColor(Color.parseColor(l.color));
                canvas.drawLine(l.firstX,l.firstY,l.secondX,l.secondY,paint);

            }
            System.out.println("----->"+String.valueOf(System.currentTimeMillis()-startTime));
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            mWidth = widthSize;
            mHeight = heightSize;
            setMeasuredDimension(mWidth, mHeight);
            if(!initFlag)
            {
                init();
                initFlag = true;
            }

        }

        class Point
        {
            int x;
            int y;
            double angle;
            int previousX;
            int previousY;
            float radius;
            float proxdistance;

        }

        class Line
        {
            int     firstX;
            int     secondX;
            int     firstY;
            int     secondY;
            String  color;
        }
    }
