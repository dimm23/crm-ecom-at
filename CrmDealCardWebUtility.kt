package ru.sibur.test.crm_ecom_at.web.utils.crm.Deals.DealCard

import io.qameta.allure.Allure
import io.qameta.allure.Step
import org.assertj.core.api.Assertions
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import ru.sibur.test.crm_ecom_at.common.AutoTestException
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.CrmDealUiModel
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.QuoteShortInfoUiModel
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.QuoteUiModel
import ru.sibur.test.crm_ecom_at.common.model.enums.crm.deal.CrmDealStatus
import ru.sibur.test.crm_ecom_at.web.model.WebTestContext
import ru.sibur.test.crm_ecom_at.web.pages.crm.deal.deal_card.CrmDealCardPage
import ru.sibur.test.crm_ecom_at.web.utils.BaseWebUtility

class CrmDealCardWebUtility(_webTestContext: WebTestContext,
                            _basePoint: BaseWebUtility
) : BaseWebUtility(_webTestContext) {

    init {
        this.basePoint = _basePoint
    }

    @Step("Нажатие на карточке сделки на апплете \"Продукты\" кнопки \"+\" (открывает попап \"Новый продукт\")")
    fun addQuoteItemBtnClick() {
        crmBaseUtil().waitClickable(crmDealCard.addQuoteItemBtn)
        crmDealCard.addQuoteItemBtn.click()
    }

    @Step("Нажатие кнопки \"Копировать\" на карточке сделки")
    fun copyDealBtnClick() {
        crmBaseUtil().waitClickable(crmDealCard.copyQuoteBtn)
        crmDealCard.copyQuoteBtn.click()
    }

    @Step("Открытие позиции сделки по индексу")
    fun openDealPositionByIndex(index: Int) {
        if (crmDealCard.quoteList.size == 0) throw AutoTestException("Deal haven't any position.")

        val it = crmDealCard.quoteList[index]
        val link = it.findElement(By.xpath(CrmDealCardPage.QUOTE_LIST_LINK_XPATH))
        waitClickable(link)
        try {
            link.click()
        } catch (e: Exception) {
            it.click()
            waitClickable(link)
            link.click()
        }
        crmBaseUtil().waitUiIsReady()
    }

    @Step("Заполнение полей на карточке сделки")
    fun fillDealFields(
        currency: String? = null,
        contract: String? = null,
        distribChannel: String? = null,
        comment: String? = null,
        siFlg: Boolean? = null,
        salesOffice: String? = null,
        salesOrg: String? = null,
        freeVatZoneFlg: Boolean? = null,
        o2c: String? = null,
    ){
        if (currency != null) {
            Allure.step("Выбор валюты - $currency")
            basePoint.crmBaseUtil().setSDDByContainsText(crmDealCard.currencySDD, currency)
        }
        if (distribChannel != null) {
            Allure.step("Выбор канала сбыта - $distribChannel")
            basePoint.crmBaseUtil().setSDDByContainsText(crmDealCard.distributionChannelSDD, distribChannel)
        }
        if (comment != null) {
            Allure.step("Указание коммента - $comment")
            basePoint.crmBaseUtil().setInput(crmDealCard.commentTextarea, comment)
        }
        if (siFlg != null) {
            crmBaseUtil().waitUiIsReady()
            val curSiFlg: Boolean = crmDealCard.siChBInput.findElement(By.xpath("./.."))
                .getAttribute("class").contains("sib-field_is_checked")

            if (siFlg != curSiFlg) {
                Allure.step("Требуемое значение чекбокса SI - $siFlg, текущее значение " +
                        "чекбокса SI - $curSiFlg, с этим нужно что-то делать...")
                crmDealCard.siChBInput.click()
                Allure.step("Сделан клик мышью")
            }
        }
        if (contract != null) {
            Allure.step("Выбор контракта - $contract")
            basePoint.crmBaseUtil().setSDDByContainsText(crmDealCard.contractSDD, contract)
            crmDealCard.contractSDD.sendKeys(Keys.TAB)
            crmBaseUtil().waitUiIsReady()
            try {
                crmBaseUtil().waitClickable(crmDealCard.okButton, 10)
                crmDealCard.okButton.click()
                log.info("Кликнули кнопку ОК в диалоговом сообщении")
            } catch (e: Exception) {log.info("Сообщение показано не было")}
        }
        if (salesOffice != null) {
            Allure.step("Выбор Отдела - $salesOffice", Allure.ThrowableRunnable {
                crmBaseUtil().waitUiIsReady()
                basePoint.crmBaseUtil().setSDDByContainsText(crmDealCard.salesOfficeSDD, salesOffice)
            })
        }
        if (salesOrg != null) {
            Allure.step("Выбор Сбытовой организации - $salesOrg", Allure.ThrowableRunnable {
                crmBaseUtil().waitUiIsReady()
                basePoint.crmBaseUtil().setSDDByContainsText(crmDealCard.salesOrganizationSDD, salesOrg)
            })
        }
        if (freeVatZoneFlg != null) {
            crmBaseUtil().waitUiIsReady()
            var curFreeVatZoneFlg: Boolean = crmDealCard.freeVatZoneChBInput.findElement(By.xpath("./.."))
                .getAttribute("class").contains("sib-field_is_checked")

            if (freeVatZoneFlg != curFreeVatZoneFlg) {
                Allure.step("Требуемое значение чекбокса СТЗ - $freeVatZoneFlg, текущее значение " +
                        "чекбокса СТЗ - $curFreeVatZoneFlg, с этим нужно что-то делать...")
                crmDealCard.freeVatZoneChBInput.click()
                Allure.step("Сделан клик мышью")
            }
        }
        if (o2c != null) {
            Allure.step("Выбор признака О2С - $o2c")
            basePoint.crmBaseUtil().setSDDByContainsText(crmDealCard.o2cSDD, o2c)
        }
        Allure.step("Заключительный расфокус", Allure.ThrowableRunnableVoid {
            crmDealCard.clientNameLabel.click()
        })
    }

    @Step("Ожидание загрузки карточки сделки")
    fun waitDealCardIsLoaded() {
        crmBaseUtil().waitElementToBeVisible(crmDealCard.companyNameInTitle)
        if (!crmDealCard.companyNameInTitle.isDisplayed) {
            throw AutoTestException("Карточка сделки не загрузилась, отсутствует на экране характерный элемент")
        } else {Allure.step("Создалась сделка - ${crmDealCard.dealNumberLabel.getAttribute("innerText")}")}
    }


    @Step("Получение информации с карточки сделки")
    fun getCrmDealUiInfo(): CrmDealUiModel {
        var result = CrmDealUiModel()

        result.status = crmBaseUtil().getAutoText(crmDealCard.statusInput)

        // initiator
        result.initiator.name = crmBaseUtil().getAutoText(crmDealCard.initiatorNameLabel)
        result.initiator.position = crmBaseUtil().getAutoText(crmDealCard.initiatorPositionLabel)

        result.dealNumber = crmBaseUtil().getAutoText(crmDealCard.dealNumberLabel)
        result.date = crmBaseUtil().getAutoText(crmDealCard.dealCreateDateLabel)
        result.source = crmBaseUtil().getAutoText(crmDealCard.dealSourceLabel)

        result.sumWithVat = crmBaseUtil().getAutoText(crmDealCard.sumWithVatInput)
        crmDealCard.directionInput.findElements(By.xpath(CrmDealCardPage.DEPARTMENT_INPUT_LIST_XPATH)).forEach {
            result.departments.add(crmBaseUtil().getAutoText(it))
        }

        result.account = crmBaseUtil().getAutoText(crmDealCard.clientInput)
        result.currency = crmBaseUtil().getAutoText(crmDealCard.currencySDD)
        result.contract = crmBaseUtil().getAutoText(crmDealCard.contractSDD).split(" ")[0]
        result.channel = crmBaseUtil().getAutoText(crmDealCard.distributionChannelSDD)
        result.siFlg = crmDealCard.siChBInput.findElement(By.xpath("./.."))
            .getAttribute("class").contains("sib-field_is_checked")
        result.vatFreeZone = crmDealCard.freeVatZoneChBInput.findElement(By.xpath("./.."))
            .getAttribute("class").contains("sib-field_is_checked")
        result.salesOffice = crmBaseUtil().getAutoText(crmDealCard.salesOfficeSDD)
        result.salesOrg = crmBaseUtil().getAutoText(crmDealCard.salesOrganizationSDD)

        // comment
        result.comment = crmBaseUtil().getAutoText(crmDealCard.commentTextarea)
        // o2c
        result.o2c = crmBaseUtil().getAutoText(crmDealCard.o2cSDD)

        // product (list)
        crmDealCard.quoteList.forEach {
            val pr = crmBaseUtil().getAutoText(it.findElement(
                By.xpath(CrmDealCardPage.QUOTE_LIST_LINK_XPATH))).split(" • ")
            val qty = crmBaseUtil().getAutoText(it.findElement(
                By.xpath(CrmDealCardPage.QUOTE_LIST_QTY_XPATH)))
            val price = crmBaseUtil().getAutoText(it.findElement(
                By.xpath(CrmDealCardPage.QUOTE_LIST_PRICE_XPATH)))
            val sum = crmBaseUtil().getAutoText(it.findElement(
                By.xpath(CrmDealCardPage.QUOTE_LIST_SUM_XPATH)))
            val status = crmBaseUtil().getAutoText(it.findElement(
                By.xpath(CrmDealCardPage.QUOTE_LIST_STATUS_XPATH)))
            result.products.add(
                QuoteUiModel(
                    shortInfoUiModel = QuoteShortInfoUiModel(
                    nsiCode = pr[0],
                    name = pr[1],
                    quantity = qty,
                    totalPrice = price,
                    totalSum = sum,
                    status = status,)))
        }

        // addAggreement не реализовано, потому что поля отсутствуют в модели.
        /*try {
            val addAggreementNum = crmBaseUtil().getAutoText(crmDealCard.additionalAggreementNumLabel)
                .split(" № ")[1]
        } catch (e: Exception) {log.info("Не получилось считать заголовок ДС с номером и датой")}
        try {
        val gtmNum = crmBaseUtil().getAutoText(crmDealCard.gtmNumLabel)
            .split(" № ")[1]
        } catch (e: Exception) {log.info("Не получилось считать GTM номер")}
        try {
        val rcmNum = crmBaseUtil().getAutoText(crmDealCard.rcmNumLabel)
            .split(" № ")[1]
        } catch (e: Exception) {log.info("Не получилось считать RCM номер")}*/

        return result
    }

    @Step("Получение информации по позициям сделки")
    fun getAllPositionsFromDeal(): List<QuoteUiModel> {
        var result = mutableListOf<QuoteUiModel>()

        for (i in 0..crmDealCard.quoteList.size - 1) {
            val it = crmDealCard.quoteList[i]
            val link = it.findElement(By.xpath(CrmDealCardPage.QUOTE_LIST_LINK_XPATH))
            waitClickable(link)
            try {
                link.click()
            } catch (e: Exception) {
                it.click()
                waitClickable(link)
                link.click()
            }
            crmBaseUtil().waitUiIsReady()

            val quoteHeadInfoUiModel = crmQuoteItemCardUtil().getQuoteHeadInfo()
            val quoteCommandInfoUiModel = crmQuoteItemCardUtil().getQuoteCommandInfo()

            Allure.step("Проверка развернутости вкладки \"Основная информация\"", Allure.ThrowableRunnable {
                if (!crmQuoteItemCardUtil().isGeneralInformationTabExpanded()) {
                    crmQuoteItemCardUtil().openGeneralInformationTab()
                }})
            val quoteGeneralInfoTabUiModel = crmQuoteItemCardGeneralInformationTabUtil().getGeneralInformationTabInfo()

            Allure.step("Проверка развернутости вкладки \"Доставка\"", Allure.ThrowableRunnable {
                if (!crmQuoteItemCardUtil().isDeliveryTabExpanded()) {
                    crmQuoteItemCardUtil().openDeliveryTab()
                }})
            val quoteDeliveryTabUiModel = crmQuoteItemCardDeliveryTabUtil().getDeliveryFieldValues()

            Allure.step("Проверка развернутости вкладки \"Ценообразование\"", Allure.ThrowableRunnable {
                if (!crmQuoteItemCardUtil().isPricingTabExpanded()) {
                    crmQuoteItemCardUtil().openPricingTab()
                }})
            val quotePricingTabUiModel = crmQuoteItemCardPricingTabUtil().getPricingFieldValues()

            result.add(
                QuoteUiModel(
                    headInfoUiModel = quoteHeadInfoUiModel,
                    commandInfoUiModel = quoteCommandInfoUiModel,
                    generalInfoTabUiModel = quoteGeneralInfoTabUiModel,
                    deliveryTabUiModel = quoteDeliveryTabUiModel,
                    pricingTabUiModel = quotePricingTabUiModel
                )
            )

            // back to deal card
            Allure.step("Возвращение в карточку сделки по линке с её номером.", Allure.ThrowableRunnable {
                crmQuoteItemCardUtil().goBackFromProductToDeal()
                crmBaseUtil().waitUiIsReady()
            })
        }
        return result
    }
    fun getDataFromDeal(deal2: CrmDealUiModel): CrmDealUiModel {
        waitVisibility(crmDealCard.commentTextarea)
        deal2.siFlg = crmDealCard.siChBInput.getAttribute("value").toString().equals("Y")
        deal2.channel = crmDealCard.distributionChannelSDD.getAttribute("value")

        // Смотрим код НСИ Клиента и возвращаемся обратно в сделку
        crmDealCard.clientFieldLink.click()
        basePoint.crmBaseUtil().waitUiIsReady()
        deal2.account = crmClientCardPage.codeNSI.text
        // Возвращаемся назад из карточки клиента в сделку
        crmQuoteItemCardUtil().goBackFromProductToDeal()
        basePoint.crmBaseUtil().waitUiIsReady()
        deal2.salesOffice = crmDealCard.salesOfficeSDD.getAttribute("value").split(" ")[0]
        deal2.salesOrg = crmDealCard.salesOrganizationSDD.getAttribute("value").split(" ")[0]

        return deal2
    }

    fun clickOnProduct(i: Int = 0) {
        crmBaseUtil().waitClickable(crmDealCard.productLink[i])
        crmBaseUtil().clickWithJs(crmDealCard.productLink[i])
    }

    fun assertThatCountOfProductsIsMoreThenZero() {
        try {
            crmBaseUtil().waitVisibility(By.xpath(CrmDealCardPage.PRODUCTS_ROWS_XPATH), 10)
        } catch (ex: Exception) {log.error("Не дождались появления продуктов в сделке")}

        assert(crmDealCard.productRows.size > 0) { "Продуктов в сделке " + crmDealCard.productRows.size }
    }

    fun assertThatDealAndProductHasStatus(statusValue: String) {
        Assertions.assertThat(crmDealCard.statusInput.equals(statusValue))
        // Костыль - если ожидаем что статус сделки Загружена в САП, то лучше проверять по цвету плашки, а то текст равен статусу Доп Соглашения
        if (statusValue == CrmDealStatus.UPLOADED_TO_SAP.eng || statusValue == CrmDealStatus.UPLOADED_TO_SAP.rus) {
            for (row: WebElement in crmDealCard.productRows) {
                val productStatus: String = row.findElement(By.xpath("//div[span[@rn = 'Status']]")).getAttribute("class")
                assert(productStatus.contains("sib-status_green")) {"Статус продукта не зелёный"}
            }
        } else {
            for (row: WebElement in crmDealCard.productRows) {
                val productStatus: String = row.findElement(By.xpath("//span[@rn = 'Status']")).text
                assert(productStatus.equals(statusValue)) { "Статус продукта: $productStatus не соответствует ожидаемому: $statusValue" }
            }
        }
    }

    fun clickOnButtonCreateDs() {
        Allure.step("Клик на кнопку Создать доп соглашение", Allure.ThrowableRunnable {
            crmDealCard.createRCMBtn.click()
        })
    }

    fun fillDsFields() {
        Allure.step("Заполнение полей дополнительного соглашения:", Allure.ThrowableRunnable {
            try {
                crmDealDsPage.agrementKind.click()
                crmDealDsPage.agrementKind.click()  // почему-то не всегда открывается выпадайка, поэтому для надёжности кликаем два раза
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Вид договора' выбрали " + crmDealDsPage.agrementKind.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Вид договора'")}
            try {
                crmDealDsPage.clientsSignatoryFullName.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Подписант клиента' выбрали " + crmDealDsPage.clientsSignatoryFullName.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Подписант клиента' clientsSignatoryFullName")}
            try {
                crmDealDsPage.emplSignatorInput.click()
                ecomOrderingUtil().chooseValFromDropdown(1)
                Allure.step("В поле 'Подписант Сибура' выбрали " + crmDealDsPage.emplSignatorInput.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Подписант Сибура' emplSignatorInput")}
            try {
                crmDealDsPage.emplSignatorFullName.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Подписант Сибура' выбрали " + crmDealDsPage.emplSignatorFullName.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Подписант Сибура' emplSignatorFullName")}
            try {
                crmDealDsPage.contactPersonName.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Контактное лицо клиента' выбрали " + crmDealDsPage.contactPersonName.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Контактное лицо клиента'")}
            // Следующие два поля и так уже заполнены по умолчанию
            try {
                crmDealDsPage.houseBank.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Банк Сибура' выбрали " + crmDealDsPage.houseBank.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Банк Сибура'")}
            try {
                crmDealDsPage.accountKey.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Банковский счёт Сибура' выбрали " + crmDealDsPage.accountKey.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Банковский счёт Сибура'")}
            try {
                crmDealDsPage.rulesOfCalcDate.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Правила рассчёта срока' выбрали " + crmDealDsPage.rulesOfCalcDate.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Правила рассчёта срока'")}
            try {
                crmDealDsPage.factoryCalendar.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Производственный календарь' выбрали " + crmDealDsPage.factoryCalendar.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Производственный календарь'")}
            try {
                crmDealDsPage.accSignatorInput.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Подписант клиента' выбрали " + crmDealDsPage.accSignatorInput.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Подписант клиента'")}
            try {
                crmDealDsPage.accContactPerson.click()
                ecomOrderingUtil().chooseValFromDropdown(0)
                Allure.step("В поле 'Контактное лицо клиента' выбрали " + crmDealDsPage.accContactPerson.getAttribute("value"))
            } catch (e: Exception) {Allure.step("В этом типе ДС отсутствует поле 'Контактное лицо клиента'")}

            // Кликаем Сохранить
            Allure.step("Клик на кнопку createButton", Allure.ThrowableRunnable { crmDealDsPage.createButton.click() })
        })
    }
}