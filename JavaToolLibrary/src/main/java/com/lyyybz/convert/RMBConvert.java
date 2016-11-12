package com.lyyybz.convert;

import java.util.HashMap;

/**
 * 数字金额转换成中文大写金额
 */

public class RMBConvert
{
	/**
	 * 人民币大写单位制
	 */
	private static HashMap<Integer, String> dws;
	/**
	 * 
	 * 数字对应的中文
	 */
	private static String[] jes;
	// 初始化执行
	static
	{
		dws = new HashMap<Integer, String>();
		dws.put(-2, "分");
		dws.put(-1, "角");
		dws.put(0, "元");
		dws.put(1, "拾");
		dws.put(2, "佰");
		dws.put(3, "仟");
		dws.put(4, "万");//
		dws.put(5, "拾");
		dws.put(6, "佰");
		dws.put(7, "仟");
		dws.put(8, "亿");//
		dws.put(9, "拾");
		dws.put(10, "佰");
		dws.put(11, "仟");
		dws.put(12, "万");
		jes = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	}

	/**
	 * 数字转换人民币大写
	 * 
	 * @param number
	 *            数字 不支持科学数字
	 * @return
	 */
	public static String chinese(String number)
	{
		StringBuffer su = new StringBuffer();
		// 整数部分
		number = delInvalidZero(number);
		String integral = null;
		// 小数部分
		String decimal = null;
		if (number.contains("."))
		{
			// 截取整数位
			integral = number.split("\\.")[0];
			decimal = number.split("\\.")[1];
		} else
		{
			integral = number;
		}
		// 判断是否存在整数位
		if (integral.length() > 0)
		{
			for (int i = 0; i < integral.length(); i++)
			{
				String context = integral.substring(i, i + 1);
				int pow = integral.length() - i - 1;
				Integer val = Integer.parseInt(context.toString());
				// 获取中文单位
				String sign = dws.get(pow);
				// 获取中文数字
				String name = jes[Integer.parseInt(context)];
				if (val == 0)
				{
					if (pow % 4 != 0)
					{// 删除单位
						sign = "";
					}
					if (i < integral.length() - 1)
					{
						Integer val1 = Integer.parseInt(integral.substring(i + 1, i + 2));
						if (val == 0 && val == val1)
						{
							name = "";
						}
					} else if (i == integral.length() - 1)
					{
						name = "";
					}
				}
				su.append(name + sign);
			}
		}
		// 判断是否存在小数位
		if (decimal != null)
		{
			String str = decimal.substring(0, 1);
			if (!"0".equals(str))
			{
				su.append(jes[Integer.parseInt(str)] + dws.get(-1));
			} else
			{
				if (integral.length() > 0)
				{
					su.append("整");// 12.0这种的
				} else
				{
					// 0.0
				}
			}
			if (decimal.length() == 2)
			{
				str = decimal.substring(1, 2);
				if (!"0".equals(str))
				{
					su.append(jes[Integer.parseInt(str)] + dws.get(-2));
				}
			}
		} else
		{
			if(integral.length() != 0)
			{
				su.append("整");
			}
		}
		if(su.toString().compareTo("") == 0)
		{
			return "零元";
		}
		return su.toString();
	}

	/**
	 * 清理数字特殊字符
	 */
	private static String delInvalidZero(String str)
	{
		if(str.length() == 0)
		{
			return "";
		}
		if ((str.length()==1 && "0".compareTo(str)==0) || "0".equals(str.substring(0, 1)))
		{ 
			return delInvalidZero(str.substring(1, str.length()));	// 去掉前边的0
		} else if (str.contains(","))
		{
			return delInvalidZero(str.replaceAll(",", "")); 		// 去掉中间的，
		} else
		{
			return str;
		}
	}
}
