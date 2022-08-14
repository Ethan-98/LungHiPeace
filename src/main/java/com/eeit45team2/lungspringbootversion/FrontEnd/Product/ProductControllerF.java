package com.eeit45team2.lungspringbootversion.FrontEnd.Product;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import com.eeit45team2.lungspringbootversion.backend.order.model.Cart;
import com.eeit45team2.lungspringbootversion.backend.order.service.OrderService;
import com.eeit45team2.lungspringbootversion.backend.product.model.ProductBean;
import com.eeit45team2.lungspringbootversion.backend.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@SessionAttributes(value = {"cart"})
@RequestMapping("/Front")
public class ProductControllerF {

    private ProductService productService;
    private MemberService memberService;
    private OrderService orderService;

    @Autowired
    public ProductControllerF(ProductService productService, MemberService memberService, OrderService orderService) {
        this.productService = productService;
        this.memberService = memberService;
        this.orderService = orderService;
    }

    //顯示商品
//    @GetMapping("/products")
//    public ModelAndView getAllProducts() {
//        ModelAndView modelAndView = new ModelAndView("/FrontEnd/Shop/shop"); //對應的html檔路徑
//        modelAndView.addObject("products", productService.findAll());
//        return modelAndView;
//    }
    @GetMapping("/products")
    public String showProducts(@ModelAttribute("cart") Cart cart, Model model, Principal principal) {
        model.addAttribute("products", productService.findAll());
        return "FrontEnd/Shop/shop";

    }

    @GetMapping("/ordersview")
    public String showOrders() {
        return "FrontEnd/Order/order-account";
    }

    @GetMapping("/Cart")
    public String viewcart(@ModelAttribute Cart cart) {
        return "FrontEnd/Shop/cart2";
    }

    @GetMapping("CheckOut")
    public String viewCheckOut(@ModelAttribute("cart") Cart cart,
                               Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        MemberBean memberBean = memberService.findByMiAccount(principal.getName());
        model.addAttribute("MiCity", memberBean.getMiCity());
        model.addAttribute("MiDistrict", memberBean.getMiDistrict());
        model.addAttribute("MiAddress", memberBean.getMiAddress());
        model.addAttribute("MiName", memberBean.getMiName());
        model.addAttribute("MiPhone", memberBean.getMiPhone());
        model.addAttribute("MiEmail", memberBean.getMiEmail());
        return "FrontEnd/Shop/CheckOut";
    }

    @GetMapping("/productDetail/{id}")
    public ModelAndView showFormForView(@PathVariable Integer id) {
        ModelAndView mav1 = new ModelAndView("/FrontEnd/Product/single-product");
        ProductBean productBean = productService.FindById(id);
        mav1.addObject("product", productBean);
        return mav1;
    }

//    @GetMapping("/products")
//    public String showProducts(@ModelAttribute("cart") Cart cart, Model model, Principal principal,String items) {
//        model.addAttribute("products", productService.FindByItems(items));
//        return "FrontEnd/Shop/shop";
//
//    }


    @ModelAttribute
    public Cart cart(@ModelAttribute Cart cart) {
        if (cart == null) {
            return new Cart();
        }
        return cart;
    }

}
