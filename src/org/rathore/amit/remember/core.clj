(ns org.rathore.amit.remember.core
  (:use org.rathore.amit.remember.authentication)
  (:import (org.jets3t.service.impl.rest.httpclient RestS3Service)))

(declare *s3-creds* *s3-service*)

(defmacro with-s3-creds [[access-key secret-key] & exprs]
  `(binding [*s3-creds* (s3-creds ~access-key ~secret-key)]
     (binding [*s3-service* (RestS3Service. *s3-creds*)]
       (try
        (do ~@exprs)
        (finally
         (.shutdown *s3-service*))))))

(defn all-buckets []
  (seq (.listAllBuckets *s3-service*)))

(defn get-bucket [bucket-name]
  (.getBucket *s3-service* bucket-name))

(defn create-bucket [bucket-name]
  (.createBucket *s3-service* bucket-name))

(defn put-object [bucket s3-object]
  (.putObject *s3-service* bucket s3-object))

(defn list-objects
  ([bucket]
     (seq (.listObjects *s3-service* bucket)))
  ([bucket prefix]
     (seq (.listObjects *s3-service* bucket prefix nil)))
  ([bucket prefix delimiter]
     (seq (.listObjects *s3-service* bucket prefix delimiter))))

(defn get-object [bucket key]
  (.getObject *s3-service* bucket key))

