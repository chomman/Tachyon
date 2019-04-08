<?php

	require_once 'DbConnect.php';

	$response = array();

	if(isset($_GET['apicall']))
	{
		switch($_GET['apicall'])
		{
			case 'new':

				if(isTheseParametersAvailable(array('pid','id','name','gender','dob','address','password')))
				{
					$pid = $_POST['pid'];	
					$id = $_POST['id'];
					$name = $_POST['name'];
					$gender = $_POST['gender'];
					$dob = $_POST['dob'];
					$address = $_POST['address'];
					$password  = $_POST['password'];

					$stmt = $conn->prepare("SELECT name FROM Patient WHERE pid = ? ");
					$stmt->bind_param("s", $pid);
					$stmt->execute();
					$stmt->store_result();

					if($stmt->num_rows > 0){
						$response['error'] = true;
						$response['message'] = 'User exists with this username, try something else.';
						$stmt->close();
					}
					else
					{
						$stmt = $conn->prepare("INSERT INTO Patient VALUES (?,?, ?, ?, ?, ?,?)");
						$stmt->bind_param("sssssss",$pid, $id, $name, $gender,$dob, $address,$password);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT id,name,gender,dob,address FROM Patient WHERE pid = ?");
							$stmt->bind_param("s",$pid);
							$stmt->execute();
							$stmt->bind_result( $id, $name, $gender,$dob,$address) ;

							$stmt->fetch();
							$user = array(
								'id'=>$id,
								'name'=>$name,
								'gender'=>$gender,
								'dob'=>$dob,
								'address'=>$address
							);

							$stmt->close();

							$response['error'] = false;
							$response['message'] = 'User registered successfully';
							$response['user'] = $user;
						}
						else
						{
							$response['message'] = 'User not registered successfully';

						}
						
						
					}
				$conn->close();

				}
				else
				{
					$response['error'] = true;
					$response['message'] = 'required parameters are not available';

				}
						echo json_encode($response);

			break;

			case 'existing':

				if($_SERVER['REQUEST_METHOD']=='POST')
				{

					if(isset($_POST["pid"]) && isset($_POST["password"]))
					{
						$pid= $_POST["pid"];
						$entered_password = $_POST["password"];
						$stmt = $conn->prepare("SELECT password FROM Patient WHERE pid = ? ");
						$stmt->bind_param("s", $pid);
						$stmt->execute();
						$stmt->store_result();
						$stmt->bind_result($pass) ;
						$stmt->fetch();

						if($stmt->num_rows > 0){
							$stmt->close();
							if($pass == $entered_password)
							{
								$response["username"] = $pid;
								$response["login"] = true;
								$response["message"] = "Credentials verified";
							}
							else
							{
								$response["login"] = false;
								$response["message"] ="Wrong Credentials";

							}
						}
						else
						{
							$response["login"] = false;
							$response["message"] = "User doesn't exist";
							
						}
				
					
					}
					else
					{
						$response["login"] = false;
						$response['message'] = "Enter userid and password";
					}

				}
				else{
					$response['login']=false;
					$response['message'] = "Server Error";
				}
				echo json_encode($response);

			break;

			case 'hospiget':

					
					$stmt = $conn->prepare("SELECT * FROM hospital_list where 1");
					$stmt->execute();
					$stmt->store_result();
					$stmt->bind_result($hid, $hname, $haddress);

					while($stmt->fetch()) {
					    $hlist[] = array('hid'=>$hid, 
					           'name'=>$hname, 
					           'address'=>$haddress);
					}


					if($stmt->num_rows > 0){
						$response["data"] = $hlist;
						$response["flag"]=true;
						
					}
					else
					{
						$response["flag"] = false;
						
					}
				echo json_encode($response);
					
				break;
				case 'guiding':
					
				if($_SERVER['REQUEST_METHOD']=='POST')
				{
					
					if(isset($_POST['source']))
					{
						$ssrc = $_POST['source'];
						copy("dest.txt","sd.txt");
						$myfile = fopen("sd.txt", "a");

						fwrite($myfile," ".$ssrc);
						fclose($myfile);
						copy("file.txt","final.txt");
						$fp = fopen("final.txt", "a+");
						$fp1 = file_get_contents("sd.txt");
						fwrite($fp,$fp1);
						exec('./a.out < final.txt 2>&1',$output);
						$response["data"] = array(1=>$output[0],
							2=>$output[1]);

					}
					if(isset($_POST['destination']))
					{
						$ssrc = $_POST['destination'];
						$myfile = fopen("dest.txt", "w");
						fwrite($myfile,$ssrc);
						fclose($myfile);
					}
					echo json_encode($response);

				}

				break;
				case 'getdoctor':
				if($_SERVER['REQUEST_METHOD']=='POST')
				{
					
					if(isset($_POST["hid"]))
					{
						$hid= $_POST["hid"];
						$stmt = $conn->prepare("SELECT did,name,spl FROM doctor_list WHERE hid = ? ");
						$stmt->bind_param("s", $hid);
						$stmt->execute();
						$stmt->store_result();
						$stmt->bind_result($did,$dname,$dspl) ;
						while($stmt->fetch()) 
						{
					   	 $dlist[] = array('did'=>$did, 
					           'name'=>$dname,
					           'spl'=> $dspl
					       		);
						}


						if($stmt->num_rows > 0)
						{
							$response["data"] = $dlist;
							$response["flag"]=true;
							
						}
						else
						{
							$response["flag"] = false;
							
						}
				
					
					}
				echo json_encode($response);

				}
				break;
				case 'gettimings':
				if($_SERVER['REQUEST_METHOD']=='POST')
				{
					
					if(isset($_POST["did"]) && isset($_POST["hid"]))
					{
						$hid = $_POST["hid"];
						$did = $_POST["did"];
						$stmt = $conn->prepare("SELECT timestart,timeend FROM doctor_list WHERE hid = ? and did = ? ");
						$stmt->bind_param("ss", $hid,$did);
						$stmt->execute();
						$stmt->store_result();
						$stmt->bind_result($timestart,$timeend) ;
						while($stmt->fetch()) 
						{
					   		 $dlist[] = array('tstart'=>$timestart, 
					           		'tend'=>$timeend
					       				);
						}


					if($stmt->num_rows > 0)
					{
						$response["data"] = $dlist;
						$response["flag"]=true;
						
					}
					else
					{
						$response["flag"] = false;
						
					}
				
					
					}
				echo json_encode($response);

				}

				break;

				case 'senddata':

				if($_SERVER['REQUEST_METHOD']=='POST')
				{

						if(isset($_POST['did']) && isset($_POST['hid']) && isset($_POST['username']) && isset($_POST['tstart']) && isset($_POST['tend']))
						{
							$hid = $_POST["hid"];
							$did = $_POST["did"];
							$username = $_POST["username"];
							$tstart = $_POST["tstart"];
							$tend = $_POST["tend"];
							$stmt = $conn->prepare("SELECT name,gender,dob,address,id FROM Patient WHERE pid = ?");
							echo $conn->error;
							$stmt->bind_param("s",$username);
							$stmt->execute();
							$stmt->store_result();
							$stmt->bind_result($pname,$gender,$dob,$address,$adhaar) ;
							$stmt->fetch();
							$stmt = $conn->prepare("SELECT name FROM hospital_list WHERE hid = ? ");
							$stmt->bind_param("s", $hid);
							$stmt->execute();
							$stmt->store_result();
							$stmt->bind_result($dname) ;
							$stmt->fetch();
							$stmt = $conn->prepare("INSERT INTO appointments VALUES (?,?, ?, ?, ?, ?,?,?,?,?)");
							$stmt->bind_param("ssssssssss",$adhaar, $pname, $gender, $dob,$address, $did,$dname,$hid,$tstart,$tend);
							if($stmt->execute())
							{
								$response["flag"] = true; 

							}
							else
							{

								$response["flag"]=false;

							}



						}	
						else
						{
								$response["flag"]=false;
						}	
					echo json_encode($response);

				}	

				break;
		
		}
	}
	else
	{
		$response['error'] = true;
		$response['message'] = 'Invalid API Call';
	}

	

	


	function isTheseParametersAvailable($params){

		foreach($params as $param){
			if(!isset($_POST[$param])){
				return false;
			}
		}
		return true;
	}
?>