(ns gateway.credit.purchaseAutorization
  (:require
   [gateway.database.paymentDatabase :as database]
   [clojure.java.io :as io]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.util.http-response :as response]
   [ring.util.request :as request]
   [ring.util.response :refer [redirect]]
   [ring.util.response :refer [file-response]]
   [gateway.credit.generateCreditCard :as creditCard]
   [clojure.edn :as edn]))


(defn- enoughLimit? [cpf price]

  (let [limit (database/returnLimitCreditCard cpf)]
    (if (>= (-> limit first :limite) price)
      true
      false)))

(defn- verifypassword [cpf password]
  (let [return (database/verifyPassword cpf password)]
    return))
(defn- subtractValueOfCreditCard [cpfBuyer price]
  (let [limite (-> (database/returnLimitCreditCard cpfBuyer) first :limite)
        newLimit (- limite price)]
    (database/subtractValueCreditCard cpfBuyer newLimit)))

(defn- makePurchaseCreditCard [cpfBuyer cpfFavored price NumPlots]
  (database/makePurchaseCreditCard cpfBuyer cpfFavored price NumPlots "CC")
    (println "Compra Card Realizada" cpfBuyer cpfFavored price NumPlots))

(defn generateAleatoryPaymentSlip [price]
  (let [digit1 (rand-int 9)
        digit2 (rand-int 9)
        digit3 (rand-int 9)
        digit4 (rand-int 9)
        digit5 (rand-int 9)
        digit6 (rand-int 9)
        digit7 (rand-int 9)
        digit8 (rand-int 9)
        digit9 (rand-int 9)
        digit10 (rand-int 9)
        digit11 (rand-int 9)]
    (-> (str digit1 digit2 digit3 digit4 digit5 digit6 digit7 digit8 digit9 digit10 (* digit11 price)))))

(defn makePurchaseSlip [cpfBuyer cpfFavored price]

  (let [slip (generateAleatoryPaymentSlip price)
        id-pag (database/makePurchaseSlip cpfBuyer cpfFavored price "BB")]
        (database/insertSlip (-> id-pag first :generated_key) slip)
        (println "Compra com Efetivada" cpfBuyer cpfFavored price)
        slip))

(defn buyWithSlipPayment [request]
(makePurchaseSlip (get-in request [:params :cpfBuyer])
                    (get-in request [:params :cpfFavoreced])
                    (edn/read-string(get-in request [:params :price])))
  (response/ok "efetivado"))


(defn buyWithCreditCard [request]
  "Do the buy with credit card"
  (println "buy carddddddd--> "request)
  
  (if (= true (enoughLimit? (get-in request [:params :cpfBuyer])
                            (edn/read-string (get-in request [:params :price]))))
    (do 
      (if (not 
           (empty? 
                (verifypassword (get-in request [:params :cpfBuyer]) 
                               (get-in request [:params :password]))))
        (do  
          (subtractValueOfCreditCard (get-in request [:params :cpfBuyer])
                                     (edn/read-string(get-in request [:params :price])))
          (makePurchaseCreditCard (get-in request [:params :cpfBuyer])
                                           (get-in request [:params :cpfFavoreced])
                                           (get-in request [:params :price])
                                           (get-in request [:params :qntPlots]))          
          (response/ok "efetivado"))
       
        (response/ok "Senha incorreta")))
   (response/ok "Sem limite")  
    )
  )


