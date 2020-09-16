package com.sunshine.monitor.comm.util;

public enum VehType {
	
	LITTLE_VEH("0", "小车"),
	
	LARGE_VEH("1", "大车"),
	
	MICRO_VEH("2", "摩托/微型车"),
	
	FARM_VEH("3", "农用车"),
	
	OTHER_VEH("9", "其他车"),
	
	WEIGHT_NOMAL_VEH("B11", "重型普通半挂车"),
	
	WEIGHT_BOX_VEH("B12", "重型厢式半挂车"),
	
	WEIGHT_POT_VEH("B13", "重型罐式半挂车"),
	
	WEIGHT_FLAT_VEH("B14", "重型平板半挂车"),
	
	WEIGHT_CONTAIN_VEH("B15", "重型集装箱半挂车"),
	
	WEIGHT_UNLOAD_VEH("B16", "重型自卸半挂车"),
	
	WEIGHT_SPECIAL_VEH("B17", "重型特殊结构半挂车"),
	
	MEDIUM_NOMAL_VEH("B21", "中型普通半挂车"),
	
	MEDIUM_BOX_VEH("B22", "中型厢式半挂车"),
	
	MEDIUM_POT_VEH("B23", "中型罐式半挂车"),
	
	MEDIUM_FLAT_VEH("B24", "中型平板半挂车"),
	
	MEDIUM_CONTAIN_VEH("B25", "中型集装箱半挂车"),
	
	MEDIUM_UNLOAD_VEH("B26", "中型自卸半挂车"),
	
	MEDIUM_SPECIAL_VEH("B27", "中型特殊结构半挂车"),
	
	LIGHT_NOMAL_VEH("B31", "轻型普通半挂车"),
	
	LIGHT_BOX_VEH("B32", "轻型厢式半挂车"),
	
	LIGHT_POT_VEH("B33", "轻型罐式半挂车"),
	
	LIGHT_FLAT_VEH("B34", "轻型平板半挂车"),
	
	LIGHT_UNLOAD_VEH("B35", "轻型自卸半挂车"),
	
	WITHOUT_ORBIT_VEH("D11", "无轨电车"),
	
	WITH_ORBIT_VEH("D12", "有轨电车");
	
	private String code;
	
	private String title;
	
	private VehType(String code, String title) {
		this.code = code;
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
//	<option value="G11">
//	G11:重型普通全挂车
//	</option>
//	
//	<option value="G13">
//	G13:重型罐式全挂车
//	</option>
//	
//	<option value="G14">
//	G14:重型平板全挂车
//	</option>
//	
//	<option value="G15">
//	G15:重型集装箱全挂车
//	</option>
//	
//	<option value="G16">
//	G16:重型自卸全挂车
//	</option>
//	
//	<option value="G21">
//	G21:中型普通全挂车
//	</option>
//	
//	<option value="G22">
//	G22:中型厢式全挂车
//	</option>
//	
//	<option value="G23">
//	G23:中型罐式全挂车
//	</option>
//	
//	<option value="G24">
//	G24:中型平板全挂车
//	</option>
//	
//	<option value="G25">
//	G25:中型集装箱全挂车
//	</option>
//	
//	<option value="G26">
//	G26:中型自卸全挂车
//	</option>
//	
//	<option value="G31">
//	G31:轻型普通全挂车
//	</option>
//	
//	<option value="G32">
//	G32:轻型厢式全挂车
//	</option>
//	
//	<option value="G33">
//	G33:轻型罐式全挂车
//	</option>
//	
//	<option value="G34">
//	G34:轻型平板全挂车
//	</option>
//	
//	<option value="G35">
//	G35:轻型自卸全挂车
//	</option>
//	
//	<option value="H11">
//	H11:重型普通货车
//	</option>
//	
//	<option value="H12">
//	H12:重型厢式货车
//	</option>
//	
//	<option value="H13">
//	H13:重型封闭货车
//	</option>
//	
//	<option value="H14">
//	H14:重型罐式货车
//	</option>
//	
//	<option value="H15">
//	H15:重型平板货车
//	</option>
//	
//	<option value="H16">
//	H16:重型集装厢车
//	</option>
//	
//	<option value="H17">
//	H17:重型自卸货车
//	</option>
//	
//	<option value="H18">
//	H18:重型特殊结构货车
//	</option>
//	
//	<option value="H21">
//	H21:中型普通货车
//	</option>
//	
//	<option value="H22">
//	H22:中型厢式货车
//	</option>
//	
//	<option value="H23">
//	H23:中型封闭货车
//	</option>
//	
//	<option value="H24">
//	H24:中型罐式货车
//	</option>
//	
//	<option value="H25">
//	H25:中型平板货车
//	</option>
//	
//	<option value="H26">
//	H26:中型集装厢车
//	</option>
//	
//	<option value="H27">
//	H27:中型自卸货车
//	</option>
//	
//	<option value="H28">
//	H28:中型特殊结构货车
//	</option>
//	
//	<option value="H31">
//	H31:轻型普通货车
//	</option>
//	
//	<option value="H32">
//	H32:轻型厢式货车
//	</option>
//	
//	<option value="H33">
//	H33:轻型封闭货车
//	</option>
//	
//	<option value="H34">
//	H34:轻型罐式货车
//	</option>
//	
//	<option value="H35">
//	H35:轻型平板货车
//	</option>
//	
//	<option value="H37">
//	H37:轻型自卸货车
//	</option>
//	
//	<option value="H38">
//	H38:轻型特殊结构货车
//	</option>
//	
//	<option value="H41">
//	H41:微型普通货车
//	</option>
//	
//	<option value="H42">
//	H42:微型厢式货车
//	</option>
//	
//	<option value="H43">
//	H43:微型封闭货车
//	</option>
//	
//	<option value="H44">
//	H44:微型罐式货车
//	</option>
//	
//	<option value="H45">
//	H45:微型自卸货车
//	</option>
//	
//	<option value="H46">
//	H46:微型特殊结构货车
//	</option>
//	
//	<option value="H51">
//	H51:低速普通货车
//	</option>
//	
//	<option value="H52">
//	H52:低速厢式货车
//	</option>
//	
//	<option value="H53">
//	H53:低速罐式货车
//	</option>
//	
//	<option value="H54">
//	H54:低速自卸货车
//	</option>
//	
//	<option value="J11">
//	J11:轮式装载机械
//	</option>
//	
//	<option value="J12">
//	J12:轮式挖掘机械
//	</option>
//	
//	<option value="J13">
//	J13:轮式平地机械
//	</option>
//	
//	<option value="K11">
//	K11:大型普通客车
//	</option>
//	
//	<option value="K12">
//	K12:大型双层客车
//	</option>
//	
//	<option value="K13">
//	K13:大型卧铺客车
//	</option>
//	
//	<option value="K14">
//	K14:大型铰接客车
//	</option>
//	
//	<option value="K15">
//	K15:大型越野客车
//	</option>
//	
//	<option value="K21">
//	K21:中型普通客车
//	</option>
//	
//	<option value="K22">
//	K22:中型双层客车
//	</option>
//	
//	<option value="K23">
//	K23:中型卧铺客车
//	</option>
//	
//	<option value="K24">
//	K24:中型铰接客车
//	</option>
//	
//	<option value="K25">
//	K25:中型越野客车
//	</option>
//	
//	<option value="K31">
//	K31:小型普通客车
//	</option>
//	
//	<option value="K32">
//	K32:小型越野客车
//	</option>
//	
//	<option value="K33">
//	K33:轿车
//	</option>
//	
//	<option value="K41">
//	K41:微型普通客车
//	</option>
//	
//	<option value="K42">
//	K42:微型越野客车
//	</option>
//	
//	<option value="K43">
//	K43:微型轿车
//	</option>
//	
//	<option value="M11">
//	M11:普通正三轮摩托车
//	</option>
//	
//	<option value="M12">
//	M12:轻便正三轮摩托车
//	</option>
//	
//	<option value="M13">
//	M13:正三轮载客摩托车
//	</option>
//	
//	<option value="M14">
//	M14:正三轮载货摩托车
//	</option>
//	
//	<option value="M15">
//	M15:侧三轮摩托车
//	</option>
//	
//	<option value="M21">
//	M21:普通二轮摩托车
//	</option>
//	
//	<option value="M22">
//	M22:轻便二轮摩托车
//	</option>
//	
//	<option value="N11">
//	N11:三轮汽车
//	</option>
//	
//	<option value="Q11">
//	Q11:重型半挂牵引车
//	</option>
//	
//	<option value="Q21">
//	Q21:中型半挂牵引车
//	</option>
//	
//	<option value="Q31">
//	Q31:轻型半挂牵引车
//	</option>
//	
//	<option value="T11">
//	T11:大型轮式拖拉机
//	</option>
//	
//	<option value="T21">
//	T21:小型轮式拖拉机
//	</option>
//	
//	<option value="T22">
//	T22:手扶拖拉机
//	</option>
//	
//	<option value="T23">
//	T23:手扶变形运输机
//	</option>
//	
//	<option value="X99">
//	X99:其它
//	</option>
//	
//	<option value="Z11">
//	Z11:大型专项作业车
//	</option>
//	
//	<option value="Z21">
//	Z21:中型专项作业车
//	</option>
//	
//	<option value="Z31">
//	Z31:小型专项作业车
//	</option>
//	
//	<option value="Z41">
//	Z41:微型专项作业车
//	</option>
//	
//	<option value="Z51">
//	Z51:重型专项作业车
//	</option>
//	
//	<option value="Z71">
//	Z71:轻型专项作业车
//	</option>
	
}
