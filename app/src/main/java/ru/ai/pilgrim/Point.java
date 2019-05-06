package ru.ai.pilgrim;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.sql.Blob;
import java.util.Date;

 public class Point {
        private String description;
        private Double latitude,longitude;
        private Date pointDate;
        private Bitmap photo;
        private int id;

        /*public Point(String description, Double latitude,Double longitude,Date pointDate,
                     ImageView photo, int id) {
            this.description=description;
            this.latitude=latitude;
            this.longitude=longitude;
            this.pointDate=pointDate;
            this.photo=photo;
            this.id=id;
        }*/
        public Point(String description,Bitmap photo) {
            this.description = description;
            this.photo=photo;

        }


        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        public Double getLatitude() {
            return latitude;
        }
        public void setLatitude (Double latitude) {
         this.latitude = latitude;
        }

        public Double getLongitude() {
         return longitude;
        }
        public void setLongitude (Double longitude) {
            this.longitude = longitude;
        }

        public Date getPointDate() {
            return pointDate;
        }
        public void setPointDate (Date pointDate) {
            this.pointDate = pointDate;
        }
        public Bitmap getPhoto() {
            return photo;
        }
        public void setPhoto(Bitmap photo) {
            this.photo = photo;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
    }

