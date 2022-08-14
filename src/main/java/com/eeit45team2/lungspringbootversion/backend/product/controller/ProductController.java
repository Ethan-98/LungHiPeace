package com.eeit45team2.lungspringbootversion.backend.product.controller;


import com.eeit45team2.lungspringbootversion.backend.product.model.ProductBean;
import com.eeit45team2.lungspringbootversion.backend.product.service.ProductService;
import com.eeit45team2.lungspringbootversion.backend.product.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/Backendproduct")
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    ServletContext ctx;

    String imageLocation = "resources\\images\\productImage";
    String uploadDir = "./src/main/resources/static/BackEnd/images/product/";

    @Autowired
    public ProductController(ProductService productService, ServletContext ctx) {
        this.productService = productService;
        this.ctx = ctx;
    }


    @RequestMapping("/productlist")
    public String listProduct(Model model) {
        List<ProductBean> productBean = productService.findAll();
        model.addAttribute("products", productBean);
        return "/Backendproduct/product";
    }

    @RequestMapping("/showForm")
    public String showFormForAdd(Model model) {
        ProductBean productBean = new ProductBean();
        model.addAttribute("product", productBean);
        return "/Backendproduct/productNewForm";
    }

    @PostMapping("/saveProduct")
    public RedirectView saveProduct(ProductBean productBean,
                                    @RequestParam("image4") MultipartFile multipartFile) throws IOException {
//		System.out.println("getImage: " + (ActivityBean.getImage()==null));
        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(multipartFile.getOriginalFilename());
        productBean.setLocalfileName(fileName);
        productService.save(productBean);
        String uploadDir = "./src/main/resources/static/BackEnd/images/product/";
//		FileUploadUtil.saveFile(imageLocation, fileName, multipartFile);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return new RedirectView("/Backendproduct/productlist", true);
//		return "redirect:/Backendproduct/productlist";
    }







	/*@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute("product") ProductBean productBean) {
		Boolean isInsert = (productBean.getPd_id() ==null); // 判斷是否為insert

		ProductBean productBean1 = saveImageInDB(productBean,isInsert);  // 取得MultipartFile，把圖片以BLOB型態塞進DB //setImage( )
		saveImageInLocal(productBean1,isInsert);  // 把圖片塞到本機 && setLocalfileName()
		productService.save(productBean);
		return "redirect:/productlist";
	}*/

    @GetMapping("/updateForm/{id}")
    public ModelAndView showFormForUpdate(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("Backendproduct/productEditForm");//指向orderEditForm.html
        ProductBean productBean = productService.FindById(id);
        mav.addObject("product", productBean);
        return mav;
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.delete(id);
        return "redirect:/Backendproduct/productlist";
    }

//	@GetMapping("/productpicture/{pd_id}")
//	public ResponseEntity<byte[]> getPicture(@PathVariable("pd_id") Long pd_id) {
//		byte[] body = null;
//		ResponseEntity<byte[]> responseEntity = null;
//		MediaType mediaType = null;
//		HttpHeaders headers = new HttpHeaders();
//		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
//		ProductBean product = productService.FindById(Long.valueOf(pd_id));
//
//		// 沒有會員資料
//		if(product == null) {
//			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
//		}else {
//			// 有會員資料
//			String LocalFileName = product.getLocalfileName();
//			// 有local檔名 -> 因為csv直接匯入 || 有新增照片
//			if(LocalFileName != null) {
//				// 設定ResponseHeaders
//				/* 透過檔名 setContentType(MediaType) */
//
//				if (LocalFileName.toLowerCase().endsWith("jfif")) {
//					mediaType = MediaType.valueOf(ctx.getMimeType("dummy.jpeg"));
//				} else {
//					mediaType = MediaType.valueOf(ctx.getMimeType(LocalFileName));
//					headers.setContentType(mediaType);
//				}
//				// 設定ResponseBody
////					body = fileToByteArray("/resources/images/activity/" + LocalFileName);
//				body = fileToByteArray("/resources/images/ProductImage/" + LocalFileName);
//			}else {
//				//沒有local檔名 -> 新增不傳圖片 -> 所以要顯示預設圖片
//				body = fileToByteArray("/resources/images/product/southeast.jpg");  //如果圖片為空，就上傳預設圖片
//			}
//			responseEntity = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
//			return responseEntity;
//		}
//	}
//
//
//
//	private byte[] fileToByteArray(String path) {
//		byte[] result = null;
//		try (InputStream is = ctx.getResourceAsStream(path);
//			 ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
//			byte[] b = new byte[819200];
//			int len = 0;
//			while ((len = is.read(b)) != -1) {
//				baos.write(b, 0, len);
//			}
//			result = baos.toByteArray();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	public byte[] blobToByteArray(Blob blob) {
//		byte[] result = null;
//		try (InputStream is = blob.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
//			byte[] b = new byte[819200];
//			int len = 0;
//			while ((len = is.read(b)) != -1) {
//				baos.write(b, 0, len);
//			}
//			result = baos.toByteArray();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//
//	}
//
////MultipartFile 轉 BLOB型態 ，塞進DB
///*	public ProductBean saveImageInDB(ProductBean productBean, Boolean isInsert) {
//		MultipartFile picture = productBean.getProductImage(); //取得MultipartFile檔案
//		// setImage (建立Blob物件，交由 Hibernate 寫入資料庫)
//		if (picture != null && !picture.isEmpty()) {
//			// 如果有上傳照片
//			try {
//				byte[] b = picture.getBytes();
//				Blob blob = new SerialBlob(b);
//				productBean.setImage(blob);   //塞BLOB
//				return productBean;
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
//			}
//		}else {
//			// 如果沒有上傳照片
//			if( !isInsert ) { // 是update時
//				try {
//					productBean.setImage(productService.FindById(productBean.getPd_id()).getImage());  // 找DB中的舊照片
//					System.out.println("setImaget 成功");
//					return productBean;
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new RuntimeException("修改時 檔案上傳發生異常: " + e.getMessage());
//				}
//			} // 不是update時不用setImage
//			return productBean;
//		}
//	}*/
//
//	// 把圖片塞到本機(放在此專案下)
//	public void saveImageInLocal(ProductBean productBean, Boolean isInsert)	{
//		String contextPath = ctx.getRealPath("");
//		String savePath = contextPath + imageLocation /* + File.separator*/;
////			String savePath = contextPath + uploadDir /* + File.separator*/;
//		System.out.println("savePath: " + savePath);
//		// 建立資料夾
//		File fileSaveDir = new File(savePath); //Server端的站存資料夾 -> D:\1_iSpan\_TeamWork\teamWorkSpace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Lung-springmvc\resource\images\activityImage
//		if (!fileSaveDir.exists()) {fileSaveDir.mkdir(); /*如果沒有這個資料夾，就建立資料夾*/  }
//		// 把圖片存到本機資料夾
//		String filename = null;
//		MultipartFile picture = productBean.getProductImage();
//		// 如果有取得MultipartFile(有上傳照片)
//		if (picture != null && !picture.isEmpty()) {
//			// setLocalfileName( ) && 存圖片到本機
//			// 不管是insert 或 update都要執行
//			try {
//				filename =  System.currentTimeMillis() + "_" + picture.getOriginalFilename();  //串接檔名 -> 毫秒_原始檔名
//				File saveFile = new File(fileSaveDir, filename);
//				picture.transferTo(saveFile);  // 存到硬碟
//				productBean.setLocalfileName(filename);
//				System.out.println("LocalfileName:" + filename);
//				System.out.println("照片存到本機專案下 -> 成功");
//			} catch (IOException e) {
//				e.printStackTrace();
//				System.out.println("修改時 照片存到本機有問題");
//			}
//			// 是update時
//			if( !isInsert ) {
//				try {
//					// 刪除本機舊照片
//					deleteImageInLocal(productBean);
//				} catch (Exception e) {
//					System.out.println("修改時 刪除本機照片有問題");
//				}
//			}
//		}else {
//			// 如果沒有取得MultipartFile(沒有上傳照片)
//			if( !isInsert ) {
//				// 是update時
//				try {
//					productBean.setLocalfileName(productService.FindById(productBean.getPd_id()).getLocalfileName());
//					System.out.println("setLocalfileName 成功");
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new RuntimeException("修改時 setLocalfileName()發生異常: " + e.getMessage());
//				}
//			}else {
//				// 不是update時不用setLocalfileName
//				System.out.println("==========離開 saveHeadshotInLocal()==========");
//			}
//		}
//	}
//
//
//
//	public void deleteImageInLocal(ProductBean productBean) {
//		String contextPath = ctx.getRealPath("");
//		String filePath = contextPath + imageLocation + File.separator;
//		System.out.println("filePath: " + filePath);
//
//		String oldFilenameString = productService.FindById(productBean.getPd_id()).getLocalfileName(); //找到本地的舊檔名
//		System.out.println("oldFilenameString:" + oldFilenameString);
//		try {
//			File file = new File(filePath + oldFilenameString);
//			file.delete();
//			System.out.println("本地圖片檔已被刪除");
//			System.out.println("刪除的檔名為: " + oldFilenameString);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("刪除檔案有錯");
//		}
//	}
}

