package ru.sibur.test.crm_ecom_at.tests.steps.web.crm.dealCard

import io.qameta.allure.Allure
import io.qameta.allure.Step
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.sibur.test.crm_ecom_at.common.AutoTestException
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.CrmDealUiModel
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.QuoteUiModel
import ru.sibur.test.crm_ecom_at.common.model.enums.crm.CrmQuoteItemButtons
import ru.sibur.test.crm_ecom_at.common.properties.CommonProperties
import ru.sibur.test.crm_ecom_at.tests.context.IntegrationTestContext
import ru.sibur.test.crm_ecom_at.tests.steps.web.BaseWebSteps

@Component
class CrmQuoteItemCardSteps {
    /**
     * Шаги внутри продукта в карточке сделки.
     */
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var commonProperties: CommonProperties
    @Autowired
    private lateinit var baseWebSteps: BaseWebSteps
    @Autowired
    private lateinit var integrationTestContext: IntegrationTestContext
    @Autowired
    private lateinit var crmQuoteItemCardSteps: CrmQuoteItemCardSteps


    @Step("Заполнение Основной информации на строке сделки")
    fun setGeneralInfoFields(comment: String? = null){
        val deal: CrmDealUiModel = if (comment != null) {
            integrationTestContext.fetch(alias = comment, clazz = CrmDealUiModel::class)
        } else {
            integrationTestContext.fetch(clazz = CrmDealUiModel::class)
        }
        Allure.step("Проверка развернутости вкладки \"Основная информация\"", Allure.ThrowableRunnable {
            if (!baseWebSteps.ui().crmQuoteItemCardUtil().isGeneralInformationTabExpanded()) {
                baseWebSteps.ui().crmQuoteItemCardUtil().openGeneralInformationTab()
            }})

        val genInfoTab = deal.products[0].generalInfoTabUiModel
        baseWebSteps.ui().crmQuoteItemCardGeneralInformationTabUtil().fillGeneralInformationFields(
            product = genInfoTab.product,
            quoteType = genInfoTab.quoteType,
            fds = genInfoTab.fds,
            volume = genInfoTab.volume,
            addAgreementDate = genInfoTab.additionalAgreementDate,
//            addAgreementNum = deal.products[0].additionalAgreementNumber,
            dateFrom = genInfoTab.additionalAgreementFrom,
            dateTill = genInfoTab.additionalAgreementTill,
//            direction = deal.products[0].direction,
//            contract = deal.contract,
            producerPlant = genInfoTab.producerPlant,
            comment = genInfoTab.commment,
            customerPurchaseId = genInfoTab.customerPurchaseId,
            orderDate = genInfoTab.quoteDate,
            bankFactor = genInfoTab.bankFactor,
            insuranceCompany = genInfoTab.insuranceCompany,
            operationType = genInfoTab.operationType,
            addendemDS = genInfoTab.addendemDS,
            saleType = genInfoTab.salesType,
            salesDocType = genInfoTab.salesDocType,
//            stockExchange = deal.products[0].stockExchange,
            typeOfSale = genInfoTab.typeOfSale,
        )
    }

    @Step("Заполнение вкладки Доставка на строке сделки")
    fun setDeliveryFields(comment: String? = null){
        val deal: CrmDealUiModel = if (comment != null) {
            integrationTestContext.fetch(alias = comment, clazz = CrmDealUiModel::class)
        } else {
            integrationTestContext.fetch(clazz = CrmDealUiModel::class)
        }
        Allure.step("Проверка развернутости вкладки \"Доставка\"", Allure.ThrowableRunnable {
            if (!baseWebSteps.ui().crmQuoteItemCardUtil().isDeliveryTabExpanded()) {
                log.info("Открываем вкладку Доставка")
                baseWebSteps.ui().crmQuoteItemCardUtil().openDeliveryTab()
            }})

        val deliveryTab = deal.products[0].deliveryTabUiModel
        var shipmentPlants: String? = null
        //
        if (deal.siFlg!!) {
            if (deliveryTab.shippingPlant != null) shipmentPlants = deliveryTab.shippingPlant
        } else {
            if (deliveryTab.shippingPlantList.size > 0) {
                if (deliveryTab.shippingPlantList[0].name != null)
                    shipmentPlants = deliveryTab.shippingPlantList[0].name
                else if (deliveryTab.shippingPlantList[0].code != null) 
                    shipmentPlants = deliveryTab.shippingPlantList[0].code
            }
        }
        //
        baseWebSteps.ui().crmQuoteItemCardDeliveryTabUtil().fillDeliveryFields(
            siFlg = deal.siFlg!!,
            shippingType = deliveryTab.shipmentType,
            shipper= deliveryTab.shipper,
            shippingMethod = deliveryTab.shippingMethod,
            detailedShippingMethod = deliveryTab.detailedShippingMethod,
            shippingMethodMOXToClient = deliveryTab.shippingMethodMOXToClient,
            detailedShippingMethodMOXToClient = deliveryTab.detailedShippingMethodMOXToClient,
            consignee = deliveryTab.consignee,
            consigneeAddress = deliveryTab.consigneeAddress,
            basis = deliveryTab.basis,
            shippingCondition = deliveryTab.shipmentCondition,
            destination = deliveryTab.destination,
            shippingSchedule = deliveryTab.shipmentSchedule,
            ownershipTransfer = deliveryTab.ownershipTransfer,
            overDeliveryTolerance = deliveryTab.overDeliveryTolerance,
            underDeliveryTolerance = deliveryTab.underDeliveryTolerance,
            route = deliveryTab.route,
            shReject = deliveryTab.reject,
            customsStatus = deliveryTab.customsStatus,
            shippingPlant = shipmentPlants,
            warehouse = deliveryTab.warehouse,
            packageType = deliveryTab.packageType,
            forwarder = deliveryTab.forwarder,
            scheduledDeliveryDate = deliveryTab.scheduledDeliveryDate,
            pricingDateInvoice = deliveryTab.pricingDateInvoice,
            deliveryFrom = deliveryTab.deliveryFrom,
            deliveryTo = deliveryTab.deliveryTo,
        )
    }

    @Step("Заполнение вкладки Ценообразование на строке сделки")
    fun setPricingFields(comment: String? = null){
        val locale = integrationTestContext.fetch("locale", clazz = String::class)
        var deal = CrmDealUiModel()
        deal = if (comment != null) {
            integrationTestContext.fetch(alias = comment, clazz = CrmDealUiModel::class)
        } else {
            integrationTestContext.fetch(clazz = CrmDealUiModel::class)
        }
        Allure.step("Проверка развернутости вкладки \"Ценообразование\"", Allure.ThrowableRunnable {
            if (!baseWebSteps.ui().crmQuoteItemCardUtil().isPricingTabExpanded()) {
                baseWebSteps.ui().crmQuoteItemCardUtil().openPricingTab()
            }})
        val pricingTab = deal.products[0].pricingTabUiModel

        // Если прайс-лист = 0, то проверяем есть ли сумма в итоговой цене и ставим её или проверяем наличие ручной цены
        var price: String? = null
        if (pricingTab.priceListPrice == null ||
            pricingTab.priceListPrice!!.replace(',', '.').filter { it.isDigit() || it == '.' }
                .toDouble() == 0.0) {
            log.info("Условие по priceListPrice выполнилось. Переходим к следующей проверке.")
            if (pricingTab.totalPriceWithVat != null) {
                price = pricingTab.totalPriceWithVat
            } else {
                if (pricingTab.manualPrice != null) {
                    price = pricingTab.manualPrice
                }
            }
        } else {price = pricingTab.priceListPrice}

        baseWebSteps.ui().crmQuoteItemCardPricingTabUtil().fillPricingFields(
            siFlg = deal.siFlg!!,
//            ecomPaymentCondition = pricingTab.ecomPaymentCondition,
//            ecomBuyersPrice = pricingTab.ecomBuyersPrice,
            priceListType = pricingTab.priceListType,
            paymentCondition = pricingTab.paymentCondition.code,
            serviceType = pricingTab.serviceType,
            priceListPrice = pricingTab.priceListPrice,
//            currency = deal.currency,
            taxRate = pricingTab.taxRate,
            countryForTaxDefinition = pricingTab.countryForTaxDefinition,
            otherTaxClass = pricingTab.otherTaxClass,
            vatId = pricingTab.vatId,
            deliveryIncluded = pricingTab.deliveryIncluded,
            preApprovedPrice = pricingTab.preApprovedPrice,
            premiumOrDiscountList = pricingTab.premiumOrDiscountList,
            plannedCostList = pricingTab.plannedCostList,
            manualPriceInput = price,
            logistic = pricingTab.logistic,
        )
        // Запрос НДС если его нет и если это
        baseWebSteps.ui().crmQuoteItemCardPricingTabUtil().requestVatIfNotGotYet(locale)
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
    }

    @Step("Установка команды на строке сделки")
    fun setQuoteItemTeam(){
        val deal = integrationTestContext.fetch(clazz = CrmDealUiModel::class)

        Allure.step("Открытие попапа \"Управление командой по продукту\"", Allure.ThrowableRunnable {
            baseWebSteps.ui().crmQuoteItemCardUtil().openTeamManagementPopup()
            baseWebSteps.ui().crmQuoteItemCardUtil().waitTillTeamManagementPopupIsBeingOpen()
        })

        var frontM: String? = null
        try {
            frontM = deal.products[0].commandInfoUiModel.fm!!.name
        } catch (_: Exception) {}
        var jtBack: String? = null
        try {
            jtBack = deal.products[0].commandInfoUiModel.jtBack!!.name
        } catch (_: Exception) {}
        var jtHead: String? = null
        try {
            jtHead = deal.products[0].commandInfoUiModel.jtHead!!.name
        } catch (_: Exception) {}

        baseWebSteps.ui().crmQuoteItemCardTeamManagementPopupUtil().teamManagementPopupSetFields(
            frontManager = frontM,
            jtBack = jtBack,
            jtHead = jtHead,
            clickNextBtn = true,
        )
    }

    @Step("Отправка сделки в САП")
    fun sendToSap(){
        val locale = integrationTestContext.fetch("locale", clazz = String::class)
        var deal = integrationTestContext.fetch(clazz = CrmDealUiModel::class)
        if ((deal.siFlg!!)
            && (deal.salesOffice != null)
            && (deal.salesOffice!!.contains("410"))) {
            Allure.step("Сделка СИ + Вена -> Получить НДС из САП (если его нет)", Allure.ThrowableRunnable {
                Allure.step("Проверка развернутости вкладки \"Ценообразование\"", Allure.ThrowableRunnable {
                    if (!baseWebSteps.ui().crmQuoteItemCardUtil().isPricingTabExpanded()) {
                        baseWebSteps.ui().crmQuoteItemCardUtil().openPricingTab()
                    }
                })
                Allure.step("Проверка VAT", Allure.ThrowableRunnable {
                    baseWebSteps.ui().crmQuoteItemCardPricingTabUtil().requestVatIfNotGotYet(locale)
                })
            })
        }

        Allure.step("Отправка сделки в САП", Allure.ThrowableRunnable {
            pushButton(CrmQuoteItemButtons.SendToSap)
            baseWebSteps.ui().crmBaseUtil().waitUiIsReady(120)
        })
        // Подтверждаем логистику если отличается от тарифа СВТ
        baseWebSteps.ui().crmDealsUtil().confirmNotifyAboutBigLogisticDelta()
        // Заполняем коммент по отрицательной марже
        baseWebSteps.ui().crmDealsUtil().fillCommentToFinResultAndClickSave()
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady(150)
    }

    @Step("Запомнить номер сделки")
    fun rememberDealNumber(){
        Allure.step("Получение номера сделки из шапки позиции сделки и запись в контекст", Allure.ThrowableRunnable {
            val dealNumber = baseWebSteps.ui().crmQuoteItemCardUtil().getQuoteHeadInfo().dealNumberInQuoteInfo
            Allure.step("Номер сделки $dealNumber")
            integrationTestContext.store("DealNumber", item = dealNumber!!.toString())
        })
    }

    fun pushButton(btn: CrmQuoteItemButtons){
        when (btn) {
            CrmQuoteItemButtons.CopyProduct -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickCopyProductBtn()
                    })
            }
            CrmQuoteItemButtons.SendToSap -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickSendToSapBtn()
                    })
            }
            CrmQuoteItemButtons.Edit -> {
                Allure.step("Нажатие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        try {
                            baseWebSteps.ui().crmQuoteItemCardUtil().clickEditBtn()
                        } catch (e: Exception) {
                            // Если сделка уже была ранее открыта для редактирования, то отменяем редактирование и заново нажимаем Edit
                            try {
                                baseWebSteps.ui().crmQuoteItemCardUtil().clickCancelChangesBtn()
                                baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                                crmQuoteItemCardSteps.openProductInDeal()
                                baseWebSteps.ui().crmQuoteItemCardUtil().clickEditBtn()
                            } catch (ee:Exception){
                                throw AutoTestException("Выбранная сделка не доступна для редактирования")
                            }
                        }
                    })
            }
            CrmQuoteItemButtons.CancelChanges -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickCancelChangesBtn()
                    })
            }
            CrmQuoteItemButtons.RefreshPriceListPrice -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickRefreshPriceListPriceBtn()
                    })
            }
            CrmQuoteItemButtons.CancelInSap -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickCancelInSapBtn()
                    })
            }
            CrmQuoteItemButtons.Delete -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickDeleteBtn()
                    })
            }
            CrmQuoteItemButtons.CalculateVat -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickCalculateVatBtn()
                    })
            }
            CrmQuoteItemButtons.RequestLogisticRate -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickRequestLogisticRateBtn()
                    })
            }
            CrmQuoteItemButtons.CopyDataFromAnotherDeal -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickCopyDataFromAnotherDealBtn()
                    })
            }
            CrmQuoteItemButtons.CopyDataToAllPositions -> {
                Allure.step("Нажитие кнопки $btn",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmQuoteItemCardUtil().clickCopyDataToAllPositionsBtn()
                    })
            }
        }
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady(300)
    }

    @Step("Собираем сохранившиеся данные по сделке")
    fun comparisonDealDataWithEntered() {
        var deal2 = CrmDealUiModel()
        deal2.products.add(QuoteUiModel())

        Allure.step("Переходим из продукта в саму сделку и считываем данные с полей сделки",
            Allure.ThrowableRunnable {
                goToTheDealFromProduct()
                deal2 = baseWebSteps.ui().crmDealCardUtil().getDataFromDeal(deal2)
            })
        openProductInDeal()

        Allure.step(
            "Переходим на вкладку 'Итого' и считываем данные с полей на вкладке",
            Allure.ThrowableRunnableVoid {
                openOverallTab()
                deal2 = baseWebSteps.ui().crmQuoteItemCardOverAllUtil().getDataOnOverAllTab(deal2)
            })

        integrationTestContext.store(alias = "deal2", item = deal2)
    }

    @Step("Возвращаемся из продукта в сделку")
    fun goToTheDealFromProduct() {
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
        baseWebSteps.ui().crmQuoteItemCardUtil().goBackFromProductToDeal()
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
    }

    @Step("Открываем продукт")
    fun openProductInDeal(i:  Int = 0) {
        baseWebSteps.ui().crmDealCardUtil().clickOnProduct(i)
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
    }

    // Точно ли необходимо наличие методов ниже?
    // Часть пока не нужна, но возможно в следующих тестах понадобятся
    fun openGeneralInformationTab(){
        baseWebSteps.ui().crmQuoteItemCardUtil().openGeneralInformationTab()
    }
    fun openDeliveryTab(){
        baseWebSteps.ui().crmQuoteItemCardUtil().openDeliveryTab()
    }
    fun openPricingTab(){
        baseWebSteps.ui().crmQuoteItemCardUtil().openPricingTab()
    }
    fun openPlannedSchedulesTab(){
        baseWebSteps.ui().crmQuoteItemCardUtil().openPlannedSchedulesTab()
    }
    fun openOverallTab(){
        baseWebSteps.ui().crmQuoteItemCardUtil().openOverallTab()
    }
    //
}