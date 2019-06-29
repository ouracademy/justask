package org.ouracademy.justask;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class JustaskApplication implements CommandLineRunner {

	public static void main(String[] args) throws Exception {
		new SpringApplicationBuilder(JustaskApplication.class).headless(false).run(args);
	}

	@Override
	public void run(String... args) throws Exception {

		streamingMicRecognize();

	}

	/**
	 * Performs microphomericanne streaming speech recognition with a duration of 1
	 * minute.
	 */
	public void streamingMicRecognize() throws Exception {

		ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
		try (SpeechClient client = SpeechClient.create()) {

			responseObserver = new ResponseObserver<StreamingRecognizeResponse>() {
				ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

				public void onStart(StreamController controller) {
				}

				public void onResponse(StreamingRecognizeResponse response) {
					List<StreamingRecognitionResult> r = response.getResultsList();

					String s = r.stream().map(x -> x.getAlternatives(0).getTranscript()).collect(Collectors.joining())
							.trim().toLowerCase();

					System.out.printf("Transcript : %s\n", s);

					try {

						Robot robot = new Robot();
						System.out.print("'" + s + "'");

						if (s.startsWith("delete")) {

							String args = s.split(" ").length == 1 ? "" : s.split(" ")[1];
							if (!args.isEmpty()) {
								long repeat = Integer.parseInt(args);
								LongStream.rangeClosed(1, repeat).sequential()
										.forEach(x -> type(robot, KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SPACE));
							} else {
								type(robot, KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SPACE);
							}

						} else if (s.startsWith("comment")) {
							type(robot, KeyEvent.VK_CONTROL, KeyEvent.VK_K);
							type(robot, KeyEvent.VK_CONTROL, KeyEvent.VK_C);
						} else if (s.startsWith("remove comment")) {
							type(robot, KeyEvent.VK_CONTROL, KeyEvent.VK_K);
							type(robot, KeyEvent.VK_CONTROL, KeyEvent.VK_U);
						} else if (s.equals("down"))
							type(robot, KeyEvent.VK_DOWN);
						else if (s.equals("up"))
							type(robot, KeyEvent.VK_UP);
						else if (s.equals("."))
							type(robot, KeyEvent.VK_PERIOD);
						else if (s.equals("enter"))
							type(robot, KeyEvent.VK_ENTER);
						else {
							for (int charUnicodeIndex : s.chars().toArray()) {
								int unicodeA = 97;
								type(robot, charUnicodeIndex - unicodeA + KeyEvent.VK_A);
							}
						}

					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					// Transferable transferable = new StringSelection(s);
					// clipboard.setContents(transferable, null);

					responses.add(response);
				}

				public void onComplete() {
					for (StreamingRecognizeResponse response : responses) {
						StreamingRecognitionResult result = response.getResultsList().get(0);
						SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
						System.out.printf("Transcript : %s\n", alternative.getTranscript());
					}
				}

				public void onError(Throwable t) {
					System.out.println(t);
				}
			};

			ClientStream<StreamingRecognizeRequest> clientStream = client.streamingRecognizeCallable()
					.splitCall(responseObserver);

			RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
					.setEncoding(RecognitionConfig.AudioEncoding.LINEAR16).setLanguageCode("en-US")
					.setSampleRateHertz(16000).build();
			StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder()
					.setConfig(recognitionConfig).build();

			StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder()
					.setStreamingConfig(streamingRecognitionConfig).build(); // The first request in a streaming call
																				// has to be a
																				// config

			clientStream.send(request);
			// SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed:
			// true,
			// bigEndian: false
			AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
			DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, audioFormat); // Set the system
																								// information to
																								// read from the
																								// microphone audio
																								// stream

			if (!AudioSystem.isLineSupported(targetInfo)) {
				System.out.println("Microphone not supported");
				System.exit(0);
			}
			// Target data line captures the audio stream the microphone produces.
			TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			System.out.println("Start speaking");
			long startTime = System.currentTimeMillis();
			// Audio Input Stream
			AudioInputStream audio = new AudioInputStream(targetDataLine);
			while (true) {
				long estimatedTime = System.currentTimeMillis() - startTime;
				byte[] data = new byte[6400];
				audio.read(data);
				if (estimatedTime > 3 * 60 * 1000) { // 60 * 1000 = 60 seconds
					System.out.println("Stop speaking.");
					targetDataLine.stop();
					targetDataLine.close();
					break;
				}
				request = StreamingRecognizeRequest.newBuilder().setAudioContent(ByteString.copyFrom(data)).build();
				clientStream.send(request);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObserver.onComplete();
	}

	private void type(Robot robot, int... keys) {
		robot.delay(40);
		Arrays.stream(keys).forEach(i -> robot.keyPress(i));
		Arrays.stream(keys).forEach(i -> robot.keyRelease(i));
	}
}
