<?php

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		require_once('DbConnect.php');
		
		if(isset($_POST["image"]))
		{
			$image_encode =$_POST["image"];
      
        	$image = base64_decode(str_pad(strtr($image_encode,'-_','+/'),strlen($image_encode)%4,'=',STR_PAD_RIGHT));
			$userImage = 'simpletext.png'; // renaming image
			$path = '';  // your saving path
		 	$thumb_file = $path . $userImage;
			file_put_contents($thumb_file, $image);
			

			exec('python3 general_text.py simpletext.png 2>&1',$output);
			#var_dump($output);
				

			
			$user['data'] = array(
						'id'=>$output[3],
						'name'=>$output[2],
						'gender'=>$output[1],
						'dob'=>$output[0],
						'address'=>$output[4]
			);

	
		
		}
		else
		{
			$user['message'] = "Couldn't upload file try again";
			echo json_encode($user);
		}

		}
		else{
			$user['message'] = "Error";
			echo json_encode($user);
		}

		if(isset($user['data']))
		{
			$user['message'] = "Successful";
			echo json_encode($user);

		}
		else
		{
			$user['message'] = "Error";
			echo json_encode($user);
		}

	
?>
