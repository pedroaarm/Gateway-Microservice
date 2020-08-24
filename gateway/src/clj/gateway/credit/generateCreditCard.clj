(ns gateway.credit.generateCreditCard
    (:require 
     [clojure.string :as str]))

(defn- parse-brith [brith]
  (->> (str/split brith #"-")
       (map #(Integer/parseInt %))))

(defn- verifySumBrith [value]
  (let [month (nth value 1)
        day (nth value 2)
        sum (+ day month)]
    (if (< sum 10)
      (do
        (->
         (+ sum 10 (rand-int 20))))
      (-> sum))))



(defn- definePartByCpf [cpf]
  (let [fisrtNumber (subs cpf 10 11)
        lastNumber (subs cpf 9 10)
        thirdNumber (rand-int 9)
        secondNumber (rand-int 9)]
    (-> (str fisrtNumber secondNumber thirdNumber lastNumber))))

(defn- definePartAleatory []
  (let [fisrtNumber 4
        secondNumber (rand-int 9)
        thirdNumber (rand-int 9)
        lastNumber (rand-int 9)]
    (-> (str fisrtNumber secondNumber thirdNumber lastNumber))))

(defn- definePartbyBrith [brith]
  (let [brithArry (parse-brith brith)
        parte3-1And2 (verifySumBrith brithArry)
        parte3-3 (rand-int 9)
        parte3-4 (rand-int 9)]
    (-> (str parte3-1And2 parte3-3 parte3-4))))


(defn defineNumberCreditCard [cpf brith]
  (let [fistPart (definePartByCpf cpf)
        secondPart (definePartAleatory)
        thirdPart (definePartbyBrith brith)
        fourthPart (definePartAleatory)]
  (-> (str fistPart secondPart thirdPart fourthPart))))

(defn generateAleatoryPassword []
  (let [digit1 (rand-int 9)
        digit2 (rand-int 9)
        digit3 (rand-int 9)
        digit4 (rand-int 9)
        digit5 (rand-int 9)
        digit6 (rand-int 9)]
    (-> (str digit1 digit2 digit3 digit4 digit5 digit6))))

