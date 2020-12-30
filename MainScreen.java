package com.adcb;

import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainScreen {
	public static void main(String[] args) {
		FirstScreen fs = new FirstScreen();
		fs.init();
	}
}

class FirstScreen extends JFrame {
	static JScrollPane jsPane;
	static String _wid_type;
	static String _ccode;
	static int _min_value;
	static int _max_value;
	static String _font_name;
	static String _font_type;
	static JList<String> jList;
	static DefaultListModel<String> dlm;
	static JsonObject config_object;

	void init() {
		JFrame jFrame = new JFrame("ADCB - Kony Development Supporting Tool");

		config_object = getDefaultValues();
		System.out.println(config_object.toString());

		// Logic to Convert Pixel to Percentage
		JLabel lbl1 = new JLabel("Convert Pixel to Percentage");
		lbl1.setBounds(30, 30, 400, 40);
		jFrame.add(lbl1);

		JTextField source_px = new JTextField();
		// source_px.setToolTipText("Enter Pixel");
		source_px.setBounds(30, 60, 100, 40);
		jFrame.add(source_px);

		JLabel lbl2 = new JLabel("% Percentage");
		lbl2.setBounds(150, 60, 150, 40);
		jFrame.add(lbl2);
		source_px.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String text = source_px.getText();
				if (text.length() > 0 && onlyDigits(text, text.length())) {
					calculatePercentage(Double.parseDouble(text), lbl2, 1, null, null);
				} else {
					lbl2.setText("% Percentage");
				}
			}
		});

		// Logic to Calculate widgets percentage.
		JLabel lbl3 = new JLabel("Calculate Percentage With Default Width");
		lbl3.setBounds(30, 100, 400, 40);
		jFrame.add(lbl3);

		JTextField source_widget = new JTextField();
		// source_px.setToolTipText("Enter Pixel");
		source_widget.setBounds(30, 130, 100, 40);
		jFrame.add(source_widget);

		JTextField source_width_widget = new JTextField();
		// source_px.setToolTipText("Enter Pixel");
		source_width_widget.setBounds(150, 130, 100, 40);
		source_width_widget.setText("375");
		// source_width_widget.setEditable(false);
		jFrame.add(source_width_widget);

		JLabel lbl4 = new JLabel("% Percentage");
		lbl4.setBounds(300, 130, 150, 40);
		jFrame.add(lbl4);
		source_widget.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = source_widget.getText();
				if (text.length() > 0 && onlyDigits(text, text.length())) {
					calculatePercentage(Double.parseDouble(text), lbl4, 2, source_widget, source_width_widget);
				} else {
					lbl4.setText("% Percentage");
				}

			}
		});

		// Logic to calculate percentage based on height
		JLabel lbl5 = new JLabel("Calculate Percentage With Default Height");
		lbl5.setBounds(30, 170, 400, 40);
		jFrame.add(lbl5);

		JTextField source_widget_h1 = new JTextField();
		// source_px.setToolTipText("Enter Pixel");
		source_widget_h1.setBounds(30, 200, 100, 40);
		jFrame.add(source_widget_h1);

		JTextField source_height_widget = new JTextField();
		// source_px.setToolTipText("Enter Pixel");
		source_height_widget.setBounds(150, 200, 100, 40);
		source_height_widget.setText("671");
		// source_height_widget.setEditable(false);
		jFrame.add(source_height_widget);

		JLabel lbl6 = new JLabel("% Percentage");
		lbl6.setBounds(300, 200, 150, 40);
		jFrame.add(lbl6);
		source_widget_h1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = source_widget_h1.getText();
				if (text.length() > 0 && onlyDigits(text, text.length())) {
					calculatePercentage(Double.parseDouble(text), lbl6, 3, source_widget_h1, source_height_widget);
				} else {
					lbl6.setText("% Percentage");
				}

			}
		});

		JTextField jtxt = new JTextField(config_object.get("default_skins_path").getAsString());
		jtxt.setBounds(30, 300, 350, 40);
		jtxt.setEnabled(false);
		jFrame.add(jtxt);

		JButton choose_file = new JButton();
		choose_file.setText("Select Skins Folder");
		choose_file.setBounds(100, 250, 150, 40);
		choose_file.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser file_chooser = new JFileChooser();
				file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected_option = file_chooser.showOpenDialog(null);
				if (selected_option == JFileChooser.APPROVE_OPTION) {
					jtxt.setText(file_chooser.getSelectedFile().getAbsolutePath());
				}

				if (jsPane != null) {
					jFrame.remove(jsPane);
				}
			}
		});
		jFrame.add(choose_file);

		JLabel lbl7 = new JLabel("Font Color Code");
		lbl7.setBounds(30, 350, 100, 40);
		jFrame.add(lbl7);

		JTextField color_code = new JTextField();
		color_code.setBounds(140, 350, 100, 40);
		jFrame.add(color_code);

		// ArrayList colours_array= new ArrayList<String>() ;
		JsonArray colours = config_object.get("colour_codes").getAsJsonArray();
		String[] colours_array = new String[colours.size()];
		for (int i = 0; i < colours.size(); i++) {
			colours_array[i] = colours.get(i).getAsString();
		}

		JComboBox<String> jcb_colours = new JComboBox(colours_array);
		jcb_colours.setBounds(250, 350, 120, 40);
		jFrame.add(jcb_colours);

		JLabel lbl8 = new JLabel("Widget Type");
		lbl8.setBounds(30, 400, 100, 40);
		jFrame.add(lbl8);

		JTextField txt_widget_type = new JTextField();
		txt_widget_type.setBounds(140, 400, 100, 40);
		jFrame.add(txt_widget_type);

		JsonArray widgets = config_object.get("widget_type").getAsJsonArray();
		String[] widgets_array = new String[widgets.size()];
		for (int i = 0; i < widgets.size(); i++) {
			widgets_array[i] = widgets.get(i).getAsString();
		}
		JComboBox<String> widget_type = new JComboBox(widgets_array);
		widget_type.setBounds(250, 400, 120, 40);
		jFrame.add(widget_type);

		JLabel lbl9 = new JLabel("Min font size ");
		lbl9.setBounds(30, 450, 90, 40);
		jFrame.add(lbl9);

		JTextField min_font = new JTextField();
		min_font.setBounds(120, 450, 70, 40);
		jFrame.add(min_font);

		JLabel lbl10 = new JLabel("Max font size ");
		lbl10.setBounds(210, 450, 90, 40);
		jFrame.add(lbl10);

		JTextField max_font = new JTextField();
		max_font.setBounds(300, 450, 70, 40);
		jFrame.add(max_font);

		JLabel _lbl11 = new JLabel("BG Color ");
		_lbl11.setBounds(30, 500, 90, 40);
		jFrame.add(_lbl11);

		JTextField bg_colour = new JTextField();
		bg_colour.setBounds(130, 500, 100, 40);
		jFrame.add(bg_colour);

		JComboBox<String> _bg_colours = new JComboBox(colours_array);
		_bg_colours.setBounds(240, 500, 130, 40);
		jFrame.add(_bg_colours);

		JLabel lbl11 = new JLabel("Font Name ");
		lbl11.setBounds(30, 550, 90, 40);
		jFrame.add(lbl11);

		JTextField tbxFontName = new JTextField();
		tbxFontName.setBounds(130, 550, 100, 40);
		jFrame.add(tbxFontName);

		JsonArray font_names = config_object.get("fonts").getAsJsonArray();
		String[] font_names_array = new String[font_names.size()];
		for (int i = 0; i < font_names.size(); i++) {
			font_names_array[i] = font_names.get(i).getAsString();
		}

		JComboBox<String> font_widget_names = new JComboBox(font_names_array);
		font_widget_names.setBounds(240, 550, 130, 40);
		jFrame.add(font_widget_names);

		JLabel lbl12 = new JLabel("Font Type ");
		lbl12.setBounds(30, 600, 90, 40);
		jFrame.add(lbl12);

		JTextField tbxFontType = new JTextField();
		tbxFontType.setBounds(130, 600, 100, 40);
		jFrame.add(tbxFontType);

		JsonArray font_types = config_object.get("font_types").getAsJsonArray();
		String[] font_types_array = new String[font_types.size()];
		for (int i = 0; i < font_types.size(); i++) {
			font_types_array[i] = font_types.get(i).getAsString();
		}
		JComboBox<String> font_widget_type = new JComboBox(font_types_array);
		font_widget_type.setBounds(240, 600, 130, 40);
		jFrame.add(font_widget_type);

		JButton get_files = new JButton("Find Skins");
		get_files.setBounds(100, 650, 150, 40);
		jFrame.add(get_files);

		get_files.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String _sel_file_path = jtxt.getText().toString();
				if (color_code.getText().length() > 0) {
					_ccode = color_code.getText().toString();
				} else {
					_ccode = jcb_colours.getSelectedItem().toString();
				}
				if (txt_widget_type.getText().length() > 0) {
					_wid_type = txt_widget_type.getText().toString();
				} else {
					_wid_type = widget_type.getSelectedItem().toString();
				}

				_min_value = Integer.parseInt(min_font.getText().toString());

				_max_value = Integer.parseInt(max_font.getText().toString());

				if (tbxFontName.getText().length() > 0) {
					_font_name = tbxFontName.getText().toString();
				} else {
					_font_name = font_widget_names.getSelectedItem().toString();
				}
				if (tbxFontType.getText().length() > 0) {
					_font_type = tbxFontType.getText().toString();
				} else {
					_font_type = font_widget_type.getSelectedItem().toString();
				}

				System.out.println("File Path : " + _sel_file_path);
				System.out.println("Colour Code : " + _ccode);
				System.out.println("Widget Type : " + _wid_type);
				System.out.println("Min Value : " + _min_value);
				System.out.println("Max Value : " + _max_value);
				System.out.println("Font Name : " + _font_name);
				System.out.println("Font Type : " + _font_type);

				getSearchResult(jFrame, _sel_file_path);
			}
		});

		JButton _copy = new JButton("Copy To Clipboard");
		_copy.setBounds(600, 650, 150, 40);
		jFrame.add(_copy);

		_copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int _index = jList.getSelectedIndex();
				String selected_value = dlm.get(_index);
				StringSelection stringSelection = new StringSelection(selected_value);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});

		jFrame.setSize(800, 750);
		jFrame.setLayout(null);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static JsonObject getDefaultValues() {
		JsonObject obj = null;
		JsonParser parser = new JsonParser();
		try (FileReader reader = new FileReader("config.json")) {
			obj = (JsonObject) parser.parse(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static boolean onlyDigits(String str, int n) {
		// Traverse the string from
		// start to end
		for (int i = 0; i < n; i++) {

			// Check if character is
			// digit from 0-9
			// then return true
			// else false
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static void calculatePercentage(double px, JLabel label, int index, JTextField tbx1, JTextField tbx2) {
		switch (index) {
		case 1:
			double sum_percentage = px * 6.25;
			label.setText(sum_percentage + "% percentage");
			break;
		case 2:
			double value1 = Double.parseDouble(tbx1.getText().toString());
			double value2 = Double.parseDouble(tbx2.getText().toString());
			double per_cent = value1 / value2 * 100;
			label.setText(Math.ceil(per_cent) + "% percentage");
			break;
		case 3:
			double value3 = Double.parseDouble(tbx1.getText().toString());
			double value4 = Double.parseDouble(tbx2.getText().toString());
			double per_cent_height = value3 / value4 * 100;
			label.setText(Math.ceil(per_cent_height) + "% percentage");
			break;
		case 4:
			break;

		}

	}

	public static void getSearchResult(JFrame _frame, String file_path) {
		if (jsPane != null) {
			_frame.remove(jsPane);
		}

		dlm = new DefaultListModel();
		File file = new File(file_path);
		String[] files_list = file.list();
		JsonParser parser = new JsonParser();
		for (String _f : files_list) {
			String dest_path = file.getAbsoluteFile() + "/" + _f;
			// System.out.println("Dest Path : "+ dest_path);
			File f = new File(dest_path);
			System.out.println(f.getName());
			if (f.exists() && f.isFile() && _f.endsWith(".json")) {
				try (FileReader reader = new FileReader(f)) {
					JsonObject obj = (JsonObject) parser.parse(reader);
					if (obj != null) {
						String widget_type_ = obj.get("wType").getAsString();
						if (widget_type_.equalsIgnoreCase(_wid_type)) {
							System.out.println(obj.toString());
							String color_code_ = "";
							if (obj.has("font_color")) {
								color_code_ = obj.get("font_color").getAsString();
							}
							int font_size_ = 0;
							if (obj.has("font_size")) {
								font_size_ = obj.get("font_size").getAsInt();
							}
							String font_type_ = "";
							if (obj.has("font_style")) {
								font_type_ = obj.get("font_style").getAsString();
							}
							String font_name_ = "";
							if (obj.has("android")) {
								if (obj.get("android").getAsJsonObject().has("font_name")) {
									font_name_ = obj.get("android").getAsJsonObject().get("font_name").getAsString();
								}
							}
							System.out.println("Widget Type:" + widget_type_);
							System.out.println("Colour Code:" + color_code_);
							System.out.println("Font Size:" + font_size_);
							System.out.println("Font Name:" + font_name_);
							System.out.println("Font Type:" + font_type_);

							System.out.println(widget_type_.equalsIgnoreCase(_wid_type));
							System.out.println(_ccode.equalsIgnoreCase(color_code_));
							System.out.println((font_size_ >= _min_value && font_size_ <= _max_value));
							System.out.println(font_name_.equalsIgnoreCase(_font_name));
							System.out.println(font_type_.equalsIgnoreCase(_font_type));
							_ccode = _ccode + "ff";
							// break;
							if (widget_type_.equalsIgnoreCase(_wid_type) && _ccode.contains(color_code_)
									&& (font_size_ >= _min_value && font_size_ <= _max_value)
									&& font_name_.equalsIgnoreCase(_font_name)
									&& font_type_.equalsIgnoreCase(_font_type)) {
								dlm.addElement(_f.substring(0, _f.length() - 5));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		jList = new JList(dlm);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		// jList.setBounds(400, 30, 400, 600);
		// _frame.add(jList);

		jsPane = new JScrollPane(jList);
		jsPane.setBounds(400, 30, 300, 600);
		Container contentPane = _frame.getContentPane();
		contentPane.add(jsPane);

		SwingUtilities.updateComponentTreeUI(_frame);
	}

}
