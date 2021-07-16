(ns hyperfiddle.rcf.reporters
  (:require [clojure.stacktrace :as stack]
            [clojure.test :as t]
            [hyperfiddle.rcf.utils :as utils :refer [pprint testing-vars-str]]))

(defmethod t/report :pass [m]
  (t/with-test-out
    (t/inc-report-counter :pass)
    (print "✅")))

(defmethod t/report :fail [m]
  (t/with-test-out
    (t/inc-report-counter :fail)
    (prn)
    (print "❌ ")
    (println (str (testing-vars-str m) " "))
    (when (seq t/*testing-contexts*) (println (t/testing-contexts-str)))
    (when-let [message (:message m)] (println message))
    (prn)
    (pprint (:actual m))
    (prn)
    (print ":≠ ")
    (prn)
    (pprint (:expected m))
    (prn)
    (prn)))

(defmethod t/report :error [m]
  (t/with-test-out
    (t/inc-report-counter :error)
    (prn)
    (print "🔥 ")
    (print (str (testing-vars-str m) " "))
    (prn)
    (when (seq t/*testing-contexts*) (println (t/testing-contexts-str)))
    (when-let [message (:message m)] (println message))
    (let [actual (:actual m)]
      (if (instance? Throwable actual)
        (stack/print-cause-trace actual t/*stack-trace-depth*)
        (pprint actual)))
    (prn)
    (print ":≠ ")
    (prn)
    (prn)
    (pprint (:expected m))
    ))


