package org.example;

import net.thauvin.erik.crypto.CryptoException;
import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.text.DecimalFormat;

public class MyBot extends TelegramLongPollingBot {

    public MyBot() {
        super("6644785668:AAFa6w5npf1MCR_DLu2-NR0Kclytfe2URH0");
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        try {
            if (text.equals("/start")) {
                sendMessage(chatId,"Hi");
            } else if (text.equals("btc")) {
                sendPrice(chatId, "BTC");
                sendPhoto(chatId, "Bitcoin.png");
            } else if (text.equals("eth")) {
                sendPrice(chatId, "ETH");
                sendPhoto(chatId, "Ethereum.png");
            } else if (text.equals("doge")) {
                sendPrice(chatId,"DOGE");
                sendPhoto(chatId, "Dogecoin.png");
            } else if (text.equals("all")) {
                sendPrice(chatId, "BTC");
                sendPhoto(chatId, "Bitcoin.png");
                sendPrice(chatId, "ETH");
                sendPhoto(chatId, "Ethereum.png");
                sendPrice(chatId,"DOGE");
                sendPhoto(chatId, "Dogecoin.png");
            } else {
                try {
                    double amount = Double.parseDouble(text);
                    calculateCryptoAmount(amount, chatId);
                } catch (NumberFormatException e) {
                    sendMessage(chatId,"Unknown command!");
                }
            }
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    void sendMessage(long chatId, String text) throws Exception {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }

    void sendPrice(long chatId, String name) throws Exception {
        var price = CryptoPrice.spotPrice(name);
        sendMessage(chatId, name + " price: " + price.getAmount().doubleValue());
    }

    void sendPhoto(long chatId, String logoFileName) throws Exception {
        var photo = getClass().getClassLoader().getResourceAsStream(logoFileName);

        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(new InputFile(photo, logoFileName));
        execute(message);
    }

    private double getCryptoPrice(String cryptoName) throws IOException, CryptoException {
        return CryptoPrice.spotPrice(cryptoName).getAmount().doubleValue();
    }

    private void calculateCryptoAmount(double amount, long chatId) throws Exception {

        double btcAmount = amount / getCryptoPrice("BTC");
        double ethAmount = amount / getCryptoPrice("ETH");
        double dogeAmount = amount / getCryptoPrice("DOGE");

        DecimalFormat df = new DecimalFormat("#.####");

        sendMessage(chatId, String.format("For $%.2f you can buy:", amount));

        sendPhoto(chatId, "Bitcoin.png");
        sendMessage(chatId, "BTC: " + df.format(btcAmount));

        sendPhoto(chatId, "Ethereum.png");
        sendMessage(chatId, "ETH: " + df.format(ethAmount));

        sendPhoto(chatId, "Dogecoin.png");
        sendMessage(chatId, "DOGE: " + df.format(dogeAmount));
    }

    @Override
    public String getBotUsername() {
        return "Prog333_bot";
    }
}
