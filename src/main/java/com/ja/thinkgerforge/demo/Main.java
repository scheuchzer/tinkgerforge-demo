package com.ja.thinkgerforge.demo;

import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletLinearPoti;
import com.tinkerforge.IPConnection;

public class Main {

	private static final String HOST = "localhost";
	private static final int PORT = 4223;
	private static final String POTI_UID = "72Y"; // Change to your UID
	private static final String LCD_UID = "boo"; // Change to your UID

	public static void main(String args[]) throws Exception {
		IPConnection ipcon = new IPConnection();
		

		ipcon.connect(HOST, PORT);
		
		BrickletLinearPoti lp = new BrickletLinearPoti(POTI_UID, ipcon);
		BrickletLCD20x4 lcd = new BrickletLCD20x4(LCD_UID, ipcon);

		lcd.backlightOn();
		lcd.clearDisplay();

		lp.setPositionCallbackPeriod(50);
		BrickletLinearPoti.PositionListener positionListener = new BrickletLinearPoti.PositionListener() {
			public void position(int position) {
				int target = (int) ((double) position / (double) 100 * (double) 255);
				System.out.println(String.format("Raw=%3s, Target=%3s",
						position, target));
				try {
					lcd.writeLine((short) 0, (short) 0, String.format(
							"Raw=%3s, Target=%3s", position, target));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		lp.addPositionListener(positionListener);
		positionListener.position(lp.getPosition());

		System.out.println("Press key to exit");
		System.in.read();
		lcd.backlightOff();
		ipcon.disconnect();
	}

}
