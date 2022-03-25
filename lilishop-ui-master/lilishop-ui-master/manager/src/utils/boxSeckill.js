
export function boxSeckillStatusRender(h, params) {
  let text = "未知",
    color = "red";
  if (params.row.promotionStatus == "NEW") {
    text = "未开始";
    color = "geekblue";
  } else if (params.row.promotionStatus == "START") {
    text = "已开始";
    color = "green";
  } else if (params.row.promotionStatus == "END") {
    text = "已结束";
    color = "red";
  } else if (params.row.promotionStatus == "CLOSE") {
    text = "已关闭";
    color = "red";
  }
  return h("div", [
    h(
      "Tag",
      {
        props: {
          color: color,
        },
      },
      text
    ),
  ]);
}




