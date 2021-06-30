function changeBank(e) {
  let bankName = e.currentTarget.textContent;
  console.log(bankName);
  e.preventDefault();
  accountTag1.textContent = bankName;
}

var accountTag1 = document.getElementById("account1");
var bankDropDownItem1 = document
  .getElementById("accountBox1")
  .querySelectorAll("#accountBox1 .dropdown-item");

bankDropDownItem1.forEach((element) => {
  element.addEventListener("click", changeBank);
});
